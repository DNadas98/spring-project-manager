package net.dnadas.auth.service.oauth2;

import lombok.RequiredArgsConstructor;
import net.dnadas.auth.exception.oauth2.OAuth2ProcessingException;
import net.dnadas.auth.model.account.*;
import net.dnadas.auth.model.user.ApplicationUser;
import net.dnadas.auth.model.user.ApplicationUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class OAuth2UserAccountService extends DefaultOAuth2UserService {
  private final UserAccountDao accountDao;
  private final ApplicationUserDao applicationUserDao;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  @Transactional(rollbackFor = Exception.class)
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
    } catch (Exception e) {
      String errorMessage = e.getMessage() != null
        ? e.getMessage()
        : "An error has occurred while processing the authorization request";
      logger.error(errorMessage);
      throw new OAuth2AuthenticationException(
        new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR),
        errorMessage);
    }
  }

  private OAuth2UserAccount loadAccount(
    AccountType accountType, Map<String, Object> attributes, String emailKey, String usernameKey) {
    String email = getEmail(attributes, emailKey);
    String username = getName(attributes, usernameKey);
    ApplicationUser applicationUser = createOrReadApplicationUser(email, username);
    Optional<UserAccount> existingOauth2Account = findExistingOAuth2Account(
      accountType, applicationUser);
    if (existingOauth2Account.isPresent()) {
      return (OAuth2UserAccount) existingOauth2Account.get();
    }
    return createOAuth2UserAccount(accountType, email, applicationUser);
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

  private ApplicationUser createOrReadApplicationUser(
    String email, String username) {
    Set<UserAccount> accountsWithMatchingEmail = accountDao.findAllByEmail(email);
    if (accountsWithMatchingEmail.isEmpty()) {
      return applicationUserDao.save(new ApplicationUser(username));
    }
    UserAccount matchingAccount = accountsWithMatchingEmail.stream().toList().getFirst();
    ApplicationUser user = matchingAccount.getApplicationUser();
    return user;
  }

  private Optional<UserAccount> findExistingOAuth2Account(
    AccountType accountType, ApplicationUser applicationUser) {
    return applicationUser.getAccounts().stream().filter(
      account -> account instanceof OAuth2UserAccount &&
        account.getAccountType().equals(accountType)).findFirst();

  }

  private OAuth2UserAccount createOAuth2UserAccount(
    AccountType accountType, String email, ApplicationUser applicationUser) {
    OAuth2UserAccount newOauth2Account = OAuth2UserAccountFactory.getAccount(email, accountType);
    newOauth2Account.setApplicationUser(applicationUser);
    return accountDao.save(newOauth2Account);
  }
}
