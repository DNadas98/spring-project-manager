package com.codecool.tasx.service.auth;

import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.model.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserProvider {
  public User getAuthenticatedUser() throws UnauthorizedException {
    try {
      return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    } catch (Exception e) {
      throw new UnauthorizedException();
    }
  }
}
