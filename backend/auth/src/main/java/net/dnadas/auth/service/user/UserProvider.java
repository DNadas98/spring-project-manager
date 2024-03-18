package net.dnadas.auth.service.user;

import lombok.RequiredArgsConstructor;
import net.dnadas.auth.exception.authentication.UnauthorizedException;
import net.dnadas.auth.exception.user.UserNotFoundException;
import net.dnadas.auth.model.user.ApplicationUser;
import net.dnadas.auth.model.user.ApplicationUserDao;
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
