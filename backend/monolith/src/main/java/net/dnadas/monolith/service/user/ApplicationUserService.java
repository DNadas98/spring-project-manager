package net.dnadas.monolith.service.user;

import lombok.RequiredArgsConstructor;
import net.dnadas.monolith.dto.user.UserResponsePrivateDto;
import net.dnadas.monolith.dto.user.UserResponsePublicDto;
import net.dnadas.monolith.exception.auth.UnauthorizedException;
import net.dnadas.monolith.exception.user.UserNotFoundException;
import net.dnadas.monolith.model.user.ApplicationUser;
import net.dnadas.monolith.model.user.ApplicationUserDao;
import net.dnadas.monolith.model.user.GlobalRole;
import net.dnadas.monolith.service.auth.UserProvider;
import net.dnadas.monolith.service.converter.UserConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationUserService {
  private final ApplicationUserDao applicationUserDao;
  private final UserConverter userConverter;
  private final UserProvider userProvider;

  public UserResponsePrivateDto getOwnUserDetails() throws UnauthorizedException {
    ApplicationUser applicationUser = userProvider.getAuthenticatedUser();
    return userConverter.getUserResponsePrivateDto(applicationUser);
  }

  @Transactional(rollbackFor = Exception.class)
  public UserResponsePrivateDto updateOwnUsername(String username) {
    ApplicationUser applicationUser = userProvider.getAuthenticatedUser();
    applicationUser.setUsername(username);
    ApplicationUser updatedApplicationUser = applicationUserDao.save(applicationUser);
    return userConverter.getUserResponsePrivateDto(updatedApplicationUser);
  }

  @Transactional(rollbackFor = Exception.class)
  public void deleteOwnApplicationUser() {
    ApplicationUser user = userProvider.getAuthenticatedUser();
    applicationUserDao.delete(user);
  }

  public List<UserResponsePublicDto> getAllApplicationUsers() {
    List<ApplicationUser> users = applicationUserDao.findAll();
    return userConverter.getUserResponsePublicDtos(users);
  }

  public UserResponsePrivateDto getApplicationUserById(Long userId) throws UnauthorizedException {
    ApplicationUser user = applicationUserDao.findById(userId).orElseThrow(
      () -> new UserNotFoundException(userId)
    );
    return userConverter.getUserResponsePrivateDto(user);
  }

  @Transactional(rollbackFor = Exception.class)
  public void deleteApplicationUserById(Long id) {
    ApplicationUser user = applicationUserDao.findById(id).orElseThrow(
      () -> new UserNotFoundException(id)
    );
    if (user.getGlobalRoles().contains(GlobalRole.ADMIN)) {
      throw new UnauthorizedException();
    }
    applicationUserDao.delete(user);
  }
}
