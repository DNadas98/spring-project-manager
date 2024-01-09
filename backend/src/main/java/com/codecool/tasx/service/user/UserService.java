package com.codecool.tasx.service.user;

import com.codecool.tasx.controller.dto.user.UserResponsePrivateDto;
import com.codecool.tasx.controller.dto.user.UserUpdateRequestDto;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.user.UserNotFoundException;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.model.user.UserDao;
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
  private final UserDao userDao;
  private final UserConverter userConverter;
  private final Logger logger;
  private final UserProvider userProvider;

  public UserService(UserDao userDao, UserConverter userConverter, UserProvider userProvider) {
    this.userDao = userDao;
    this.userConverter = userConverter;
    this.userProvider = userProvider;
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  @Transactional
  public UserResponsePrivateDto getOwnUserDetails() throws UnauthorizedException {
    User user = userProvider.getAuthenticatedUser();
    return userConverter.getUserResponsePrivateDto(user);
  }

  @Transactional
  public Optional<UserResponsePrivateDto> getUserById(Long userId) throws UnauthorizedException {
    Optional<User> foundUser = userDao.findById(userId);
    if (foundUser.isEmpty()) {
      logger.error("User with ID " + userId + " was not found");
      return Optional.empty();
    }
    User user = foundUser.get();
    return Optional.of(userConverter.getUserResponsePrivateDto(user));
  }

  @Transactional(rollbackOn = Exception.class)
  public UserResponsePrivateDto updateOwnUserDetails(UserUpdateRequestDto updateRequestDto)
    throws ConstraintViolationException {
    User user = userProvider.getAuthenticatedUser();
    updateUserDetails(updateRequestDto, user);
    User updatedUser = userDao.save(user);
    return userConverter.getUserResponsePrivateDto(updatedUser);
  }

  @Transactional(rollbackOn = Exception.class)
  public UserResponsePrivateDto updateUserById(Long userId, UserUpdateRequestDto updateRequestDto)
    throws ConstraintViolationException {
    User user = userDao.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    updateUserDetails(updateRequestDto, user);
    User updatedUser = userDao.save(user);
    return userConverter.getUserResponsePrivateDto(updatedUser);
  }

  private void updateUserDetails(UserUpdateRequestDto updateRequestDto, User user) {
    //user.setScore(updateRequestDto.points());
    user.setEmail(updateRequestDto.email());
    user.setPassword(updateRequestDto.password());
    user.setUsername(updateRequestDto.username());
  }

}
