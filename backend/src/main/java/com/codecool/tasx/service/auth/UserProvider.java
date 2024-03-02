package com.codecool.tasx.service.auth;

import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.model.auth.account.UserAccount;
import com.codecool.tasx.model.user.ApplicationUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserProvider {

  @Transactional
  public ApplicationUser getAuthenticatedUser() throws UnauthorizedException {
    try {
      UserAccount userAccount =
        (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      return userAccount.getApplicationUser();
    } catch (Exception e) {
      throw new UnauthorizedException();
    }
  }
}
