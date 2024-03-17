package net.dnadas.monolith.service.auth;

import lombok.RequiredArgsConstructor;
import net.dnadas.monolith.exception.auth.UnauthorizedException;
import net.dnadas.monolith.exception.user.UserNotFoundException;
import net.dnadas.monolith.model.user.ApplicationUser;
import net.dnadas.monolith.model.user.ApplicationUserDao;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProvider {
  private final ApplicationUserDao applicationUserDao;

  @Transactional(readOnly = true)
  public ApplicationUser getAuthenticatedUser() throws UnauthorizedException {
    try {
      Long userId =
        (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      ApplicationUser user = applicationUserDao.findById(userId).orElseThrow(
        () -> new UserNotFoundException());
      return user;
    } catch (Exception e) {
      throw new UnauthorizedException();
    }
  }
}
