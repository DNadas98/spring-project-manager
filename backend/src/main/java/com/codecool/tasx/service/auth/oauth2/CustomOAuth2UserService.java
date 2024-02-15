package com.codecool.tasx.service.auth.oauth2;

import com.codecool.tasx.exception.auth.AccountAlreadyExistsException;
import com.codecool.tasx.exception.auth.AccountLinkingRequiredException;
import com.codecool.tasx.exception.auth.OAuth2ProcessingException;
import com.codecool.tasx.model.auth.account.*;
import com.codecool.tasx.model.user.ApplicationUser;
import com.codecool.tasx.model.user.ApplicationUserDao;
import jakarta.transaction.Transactional;
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

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
  private final UserAccountDao accountDao;
  private final ApplicationUserDao applicationUserDao;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  @Transactional(rollbackOn = Exception.class)
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
    try {
      OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
      Map<String, Object> attributes = oAuth2User.getAttributes();
      String providerId = oAuth2UserRequest.getClientRegistration().getRegistrationId();

      if (providerId.equals(AccountType.GOOGLE.getProviderId())) {
        return loadAccount(AccountType.GOOGLE, attributes, "email", "name");
      } else if (providerId.equals(AccountType.GITHUB.getProviderId())) {
        return loadAccount(AccountType.GITHUB, attributes, "email", "name");
      } else if (providerId.equals(AccountType.FACEBOOK.getProviderId())) {
        return loadAccount(AccountType.FACEBOOK, attributes, "email", "name");
      }
      throw new OAuth2ProcessingException("Unsupported provider: " + providerId);
    } catch (OAuth2ProcessingException | AccountLinkingRequiredException e) {
      logger.error(e.getMessage());
      throw new OAuth2AuthenticationException(
        new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR),
        e.getMessage());
    }
  }

  private OAuth2UserAccount loadAccount(
    AccountType accountType,
    Map<String, Object> attributes, String emailKey, String usernameKey) {
    String email = getEmail(attributes, emailKey);
    String username = getName(attributes, usernameKey);
    ApplicationUser applicationUser = createOrReadApplicationUser(email,
      username, accountType);
    OAuth2UserAccount oAuth2UserAccount = OAuth2UserAccountFactory.getAccount(email, accountType);
    oAuth2UserAccount.setApplicationUser(applicationUser);
    return accountDao.save(oAuth2UserAccount);
  }

  private ApplicationUser createOrReadApplicationUser(
    String email, String username, AccountType accountType) {
    List<UserAccount> accountsWithMatchingEmail = accountDao.findAllByEmail(email);
    if (accountsWithMatchingEmail.isEmpty()) {
      return applicationUserDao.save(new ApplicationUser(username));
    } else if (accountsWithMatchingEmail.stream().anyMatch(
      account -> account.getAccountType().equals(accountType))) {
      throw new AccountAlreadyExistsException();
    } else {
      return accountsWithMatchingEmail.getFirst().getApplicationUser();
    }
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
