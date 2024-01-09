package com.codecool.tasx.service.auth;


import com.codecool.tasx.controller.dto.user.auth.*;
import com.codecool.tasx.exception.auth.AccountLinkingRequiredException;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.model.user.UserDao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
  private final PasswordEncoder passwordEncoder;
  private final UserDao userDao;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Autowired
  public AuthenticationService(
    PasswordEncoder passwordEncoder, UserDao userDao, JwtService jwtService,
    AuthenticationManager authenticationManager) {
    this.passwordEncoder = passwordEncoder;
    this.userDao = userDao;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }

  @Transactional(rollbackOn = Exception.class)
  public void register(RegisterRequestDto registerRequest) {
    Optional<User> duplicateUser = userDao.findOneByEmail(registerRequest.email());
    if (duplicateUser.isPresent() && duplicateUser.get().isOAuth2User()) {
      throw new AccountLinkingRequiredException(
        "OAuth2 user account already exists, account linking is required to proceed");
    }
    User user = new User(registerRequest.username(), registerRequest.email(),
      passwordEncoder.encode(registerRequest.password()));
    userDao.save(user);
  }

  public LoginResponseDto login(LoginRequestDto loginRequest) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
    User user = getUserByEmail(loginRequest.email());
    if (user.isOAuth2User()) {
      throw new AccountLinkingRequiredException(
        "OAuth2 user account already exists, account linking is required to proceed");
    }
    String accessToken = jwtService.generateAccessToken(user);
    return new LoginResponseDto(
      accessToken,
      new UserInfoDto(
        user.getActualUsername(),
        user.getEmail(),
        user.getRoles()
      ));
  }

  private User getUserByEmail(String email)
    throws UsernameNotFoundException {
    User user = userDao.findOneByEmail(email).orElseThrow(
      () -> new UsernameNotFoundException("User account not found"));
    return user;
  }

  public String getNewRefreshToken(String email) {
    User user = getUserByEmail(email);
    return jwtService.generateRefreshToken(user);
  }

  public RefreshResponseDto refresh(RefreshRequestDto refreshRequest) {
    String refreshToken = refreshRequest.refreshToken();
    String email = jwtService.extractSubjectFromRefreshToken(refreshToken);
    User user = getUserByEmail(email);
    if (!jwtService.isRefreshTokenValid(refreshToken, user)) {
      throw new UnauthorizedException("Refresh token is invalid");
    }
    String accessToken = jwtService.generateAccessToken(user);
    return new RefreshResponseDto(
      accessToken,
      new UserInfoDto(
        user.getActualUsername(),
        user.getEmail(),
        user.getRoles()
      ));
  }
}

