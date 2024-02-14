package com.codecool.tasx.service.auth.oauth2;

import com.codecool.tasx.exception.auth.AccountLinkingRequiredException;
import com.codecool.tasx.exception.auth.OAuth2ProcessingException;
import com.codecool.tasx.model.auth.account.*;
import com.codecool.tasx.model.user.ApplicationUser;
import com.codecool.tasx.model.user.ApplicationUserDao;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
  private final UserAccountDao accountDao;
  private final ApplicationUserDao applicationUserDao;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
    try {
      OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
      Map<String, Object> attributes = oAuth2User.getAttributes();
      String providerId = oAuth2UserRequest.getClientRegistration().getRegistrationId();

      if (providerId.equals(AccountType.GOOGLE.getProviderId())) {
        return loadGoogleAccount(attributes);
      } else if (providerId.equals(AccountType.GITHUB.getProviderId())) {
        return loadGithubAccount(attributes);
      } else if (providerId.equals(AccountType.FACEBOOK.getProviderId())) {
        return loadFacebookAccount(attributes);
      }
      throw new OAuth2ProcessingException("Unsupported provider: " + providerId);
    } catch (OAuth2ProcessingException | AccountLinkingRequiredException e) {
      logger.error(e.getMessage());
      throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR),
        e.getMessage());
    }
  }

  private OAuth2UserAccount loadGoogleAccount(Map<String, Object> attributes) {
    String email = getEmail(attributes, "email");
    String username = getName(attributes, "name");

    Optional<OAuth2UserAccount> account = readAccount(email, AccountType.GOOGLE);

    final OAuth2UserAccount loadedAccount;
    if (account.isPresent()) {
      loadedAccount = account.get();
    } else {
      GoogleOAuth2UserAccount googleAccount = new GoogleOAuth2UserAccount(email);
      ApplicationUser newApplicationUser = new ApplicationUser(username);
      applicationUserDao.save(newApplicationUser);
      googleAccount.setApplicationUser(newApplicationUser);
      loadedAccount = accountDao.save(googleAccount);
    }
    return loadedAccount;
  }

  private OAuth2UserAccount loadGithubAccount(Map<String, Object> attributes) {
    String email = getEmail(attributes, "email");
    String username = getName(attributes, "name");

    Optional<OAuth2UserAccount> account = readAccount(email, AccountType.GITHUB);

    final OAuth2UserAccount loadedAccount;
    if (account.isPresent()) {
      loadedAccount = account.get();
    } else {
      GithubOAuth2UserAccount githubAccount = new GithubOAuth2UserAccount(email);
      ApplicationUser newApplicationUser = new ApplicationUser(username);
      applicationUserDao.save(newApplicationUser);
      githubAccount.setApplicationUser(newApplicationUser);
      loadedAccount = accountDao.save(githubAccount);
    }
    return loadedAccount;
  }

  private OAuth2UserAccount loadFacebookAccount(Map<String, Object> attributes) {
    String email = getEmail(attributes, "email");
    String username = getName(attributes, "name");

    Optional<OAuth2UserAccount> account = readAccount(email, AccountType.FACEBOOK);

    final OAuth2UserAccount loadedAccount;
    if (account.isPresent()) {
      loadedAccount = account.get();
    } else {
      FacebookOAuth2UserAccount facebookAccount = new FacebookOAuth2UserAccount(email);
      ApplicationUser newApplicationUser = new ApplicationUser(username);
      applicationUserDao.save(newApplicationUser);
      facebookAccount.setApplicationUser(newApplicationUser);
      loadedAccount = accountDao.save(facebookAccount);
    }
    return loadedAccount;
  }

  private Optional<OAuth2UserAccount> readAccount(
    String email, AccountType accountType) throws AccountLinkingRequiredException {
    Optional<UserAccount> account = accountDao.findOneByEmail(email);
    if (account.isEmpty()) {
      return Optional.empty();
    }
    UserAccount foundAccount = account.get();
    if (foundAccount.getAccountType() != accountType) {
      throw new AccountLinkingRequiredException(String.format(
        "%s user account with the provided e-mail address already exists. Account linking is required to proceed",
        foundAccount.getAccountType().getDisplayName()));
    }
    return Optional.of((OAuth2UserAccount) foundAccount);
  }

  private String getEmail(
    Map<String, Object> attributes, String emailKey) {
    Object emailAttribute = attributes.get(emailKey);
    if (emailAttribute == null) {
      throw new OAuth2ProcessingException("Account e-mail address is unavailable");
    }
    return emailAttribute.toString();
  }

  private String getName(
    Map<String, Object> attributes, String nameKey) {
    Object nameAttribute = attributes.get(nameKey);
    if (nameAttribute == null) {
      throw new OAuth2ProcessingException("Account name is unavailable");
    }
    return nameAttribute.toString();
  }
}
