package com.codecool.tasx.service.auth;


import com.codecool.tasx.controller.dto.user.auth.*;
import com.codecool.tasx.exception.auth.AccountAlreadyExistsException;
import com.codecool.tasx.exception.auth.AccountLinkingRequiredException;
import com.codecool.tasx.exception.auth.InvalidCredentialsException;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.model.auth.account.AccountType;
import com.codecool.tasx.model.auth.account.LocalUserAccount;
import com.codecool.tasx.model.auth.account.UserAccount;
import com.codecool.tasx.model.auth.account.UserAccountDao;
import com.codecool.tasx.model.user.ApplicationUser;
import com.codecool.tasx.model.user.ApplicationUserDao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
  private final PasswordEncoder passwordEncoder;
  private final UserAccountDao accountDao;
  private final ApplicationUserDao applicationUserDao;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Autowired
  public AuthenticationService(
    PasswordEncoder passwordEncoder, UserAccountDao accountDao,
    ApplicationUserDao applicationUserDao, JwtService jwtService,
    AuthenticationManager authenticationManager) {
    this.passwordEncoder = passwordEncoder;
    this.accountDao = accountDao;
    this.applicationUserDao = applicationUserDao;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }

  @Transactional(rollbackOn = Exception.class)
  public void register(RegisterRequestDto registerRequest) {
    Optional<UserAccount> existingAccount = accountDao.findOneByEmail(registerRequest.email());
    if (existingAccount.isPresent()) {
      UserAccount account = existingAccount.get();
      if (account.getAccountType() == AccountType.LOCAL) {
        throw new AccountAlreadyExistsException();
      }
      handleAccountLinking(existingAccount.get());
    }
    ApplicationUser newUser = new ApplicationUser(registerRequest.username());
    String hashedPassword = passwordEncoder.encode(registerRequest.password());
    UserAccount newAccount = new LocalUserAccount(registerRequest.email(), hashedPassword);
    applicationUserDao.save(newUser);
    newAccount.setApplicationUser(newUser);
    accountDao.save(newAccount);
  }

  public LoginResponseDto login(LoginRequestDto loginRequest) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
    UserAccount account = accountDao.findOneByEmailAndAccountType(
        loginRequest.email(), AccountType.LOCAL)
      .orElseThrow(() -> new InvalidCredentialsException());
    ApplicationUser user = account.getApplicationUser();
    TokenPayloadDto payloadDto = new TokenPayloadDto(account.getEmail(), account.getAccountType());
    String accessToken = jwtService.generateAccessToken(payloadDto);
    return new LoginResponseDto(
      accessToken,
      new UserInfoDto(
        user.getUsername(), account.getEmail(), account.getAccountType(), user.getGlobalRoles()));
  }

  public String getNewRefreshToken(TokenPayloadDto payloadDto) {
    return jwtService.generateRefreshToken(payloadDto);
  }

  public RefreshResponseDto refresh(RefreshRequestDto refreshRequest) {
    String refreshToken = refreshRequest.refreshToken();
    TokenPayloadDto payload = jwtService.verifyRefreshToken(refreshToken);
    UserAccount account = accountDao.findOneByEmailAndAccountType(
      payload.email(), payload.accountType()
    ).orElseThrow(() -> new UnauthorizedException());
    ApplicationUser user = account.getApplicationUser();
    String accessToken = jwtService.generateAccessToken(payload);
    return new RefreshResponseDto(
      accessToken,
      new UserInfoDto(
        user.getUsername(), account.getEmail(), account.getAccountType(), user.getGlobalRoles()));
  }

  private void handleAccountLinking(UserAccount account) {
    throw new AccountLinkingRequiredException(String.format(
      "%s user account with the provided e-mail address already exists. Account linking is required to proceed",
      account.getAccountType().getDisplayName()));
  }
}

