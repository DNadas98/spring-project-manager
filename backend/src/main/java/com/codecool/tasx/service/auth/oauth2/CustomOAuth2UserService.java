package com.codecool.tasx.service.auth.oauth2;

import com.codecool.tasx.config.auth.AccountType;
import com.codecool.tasx.exception.auth.AccountLinkingRequiredException;
import com.codecool.tasx.exception.auth.OAuth2ProcessingException;
import com.codecool.tasx.model.user.ApplicationUser;
import com.codecool.tasx.model.user.ApplicationUserDao;
import com.codecool.tasx.model.user.account.GoogleOAuth2UserAccount;
import com.codecool.tasx.model.user.account.OAuth2UserAccount;
import com.codecool.tasx.model.user.account.UserAccount;
import com.codecool.tasx.model.user.account.UserAccountDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
  private final UserAccountDao accountDao;
  private final ApplicationUserDao applicationUserDao;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
    OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
    Map<String, Object> attributes = oAuth2User.getAttributes();
    String providerId = oAuth2UserRequest.getClientRegistration().getRegistrationId();

    if (providerId.equals(AccountType.GOOGLE.getProviderId())) {
      return loadGoogleAccount(attributes);
    } /* else if (providerId.equals(AccountType.GITHUB.getProviderId())) {
      return loadGithubAccount(attributes);
    } ...*/
    throw new OAuth2ProcessingException("Unsupported provider: " + providerId);
  }

  private OAuth2UserAccount loadGoogleAccount(Map<String, Object> attributes) {
    String email = attributes.get("email").toString();
    Optional<OAuth2UserAccount> account = readAccount(email, AccountType.GOOGLE);

    final OAuth2UserAccount loadedAccount;
    if (account.isPresent()) {
      loadedAccount = account.get();
    } else {
      GoogleOAuth2UserAccount googleAccount = new GoogleOAuth2UserAccount(email);
      String username = attributes.get("name").toString();
      ApplicationUser newApplicationUser = new ApplicationUser(username);
      applicationUserDao.save(newApplicationUser);
      googleAccount.setApplicationUser(newApplicationUser);
      loadedAccount = accountDao.save(googleAccount);
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
}
