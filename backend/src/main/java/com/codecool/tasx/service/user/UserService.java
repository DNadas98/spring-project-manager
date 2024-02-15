package com.codecool.tasx.service.user;

import com.codecool.tasx.controller.dto.user.UserResponsePrivateDto;
import com.codecool.tasx.controller.dto.user.UserUpdateRequestDto;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.user.UserNotFoundException;
import com.codecool.tasx.model.user.ApplicationUser;
import com.codecool.tasx.model.user.ApplicationUserDao;
import com.codecool.tasx.service.auth.UserProvider;
import com.codecool.tasx.service.converter.UserConverter;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
  private final ApplicationUserDao applicationUserDao;
  private final UserConverter userConverter;
  private final Logger logger;
  private final UserProvider userProvider;

  public UserService(
    ApplicationUserDao applicationUserDao, UserConverter userConverter, UserProvider userProvider) {
    this.applicationUserDao = applicationUserDao;
    this.userConverter = userConverter;
    this.userProvider = userProvider;
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  @Transactional
  public UserResponsePrivateDto getOwnUserDetails() throws UnauthorizedException {
    ApplicationUser applicationUser = userProvider.getAuthenticatedUser();
    return userConverter.getUserResponsePrivateDto(applicationUser);
  }

  @Transactional
  public Optional<UserResponsePrivateDto> getUserById(Long userId) throws UnauthorizedException {
    Optional<ApplicationUser> foundUser = applicationUserDao.findById(userId);
    if (foundUser.isEmpty()) {
      logger.error("ApplicationUser with ID " + userId + " was not found");
      return Optional.empty();
    }
    ApplicationUser applicationUser = foundUser.get();
    return Optional.of(userConverter.getUserResponsePrivateDto(applicationUser));
  }

  @Transactional(rollbackOn = Exception.class)
  public UserResponsePrivateDto updateOwnUserDetails(UserUpdateRequestDto updateRequestDto)
    throws ConstraintViolationException {
    ApplicationUser applicationUser = userProvider.getAuthenticatedUser();
    updateUserDetails(updateRequestDto, applicationUser);
    ApplicationUser updatedApplicationUser = applicationUserDao.save(applicationUser);
    return userConverter.getUserResponsePrivateDto(updatedApplicationUser);
  }

  @Transactional(rollbackOn = Exception.class)
  public UserResponsePrivateDto updateUserById(Long userId, UserUpdateRequestDto updateRequestDto)
    throws ConstraintViolationException {
    ApplicationUser applicationUser = applicationUserDao.findById(userId).orElseThrow(
      () -> new UserNotFoundException(userId));
    updateUserDetails(updateRequestDto, applicationUser);
    ApplicationUser updatedApplicationUser = applicationUserDao.save(applicationUser);
    return userConverter.getUserResponsePrivateDto(updatedApplicationUser);
  }

  private void updateUserDetails(
    UserUpdateRequestDto updateRequestDto, ApplicationUser applicationUser) {
    applicationUser.setUsername(updateRequestDto.username());
  }
}
