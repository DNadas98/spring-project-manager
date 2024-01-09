package com.codecool.tasx.service.auth.oauth2;

import com.codecool.tasx.exception.auth.AccountLinkingRequiredException;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.model.user.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
  private final UserDao userDao;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest)
    throws AccountLinkingRequiredException {
    OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
    String userEmail = oAuth2User.getAttribute("email");
    String username = oAuth2User.getAttribute("name");

    Optional<User> foundUser = userDao.findOneByEmail(userEmail);
    User user;
    if (foundUser.isPresent()) {
      user = foundUser.get();
      /*if (!user.isOAuth2User()) {
        throw new AccountLinkingRequiredException(
          "Local user account already exists, account linking is required to proceed");
      }*/
    } else {
      user = userDao.save(new User(username, userEmail));
    }
    return user;
  }
}
