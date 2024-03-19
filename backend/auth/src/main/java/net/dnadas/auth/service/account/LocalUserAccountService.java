package net.dnadas.auth.service.account;


import lombok.AllArgsConstructor;
import net.dnadas.auth.dto.authentication.LoginRequestDto;
import net.dnadas.auth.dto.authentication.LoginResponseDto;
import net.dnadas.auth.dto.authentication.RegisterRequestDto;
import net.dnadas.auth.dto.authentication.UserInfoDto;
import net.dnadas.auth.dto.verification.VerificationTokenDto;
import net.dnadas.auth.exception.account.AccountAlreadyExistsException;
import net.dnadas.auth.exception.authentication.InvalidCredentialsException;
import net.dnadas.auth.model.account.AccountType;
import net.dnadas.auth.model.account.LocalUserAccount;
import net.dnadas.auth.model.account.UserAccount;
import net.dnadas.auth.model.account.UserAccountDao;
import net.dnadas.auth.model.user.ApplicationUser;
import net.dnadas.auth.model.user.ApplicationUserDao;
import net.dnadas.auth.model.verification.LocalRegistrationToken;
import net.dnadas.auth.service.JwtService;
import net.dnadas.auth.service.verification.VerificationTokenService;
import net.dnadas.email.dto.VerificationEmailRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class LocalUserAccountService {
  private final PasswordEncoder passwordEncoder;
  private final UserAccountDao accountDao;
  private final ApplicationUserDao applicationUserDao;
  private final JwtService jwtService;
  private final VerificationTokenService verificationTokenService;
  private final AuthenticationManager authenticationManager;
  private final RestTemplate restTemplate;

  @Transactional(rollbackFor = Exception.class)
  public void sendRegistrationVerificationEmail(RegisterRequestDto registerRequest) {
    VerificationTokenDto verificationTokenDto = null;
    try {
      verifyAccountDoesNotExist(registerRequest.email());
      verificationTokenService.verifyLocalRegistrationTokenDoesNotExist(registerRequest);
      String hashedPassword = passwordEncoder.encode(registerRequest.password());
      verificationTokenDto = verificationTokenService.saveLocalRegistrationToken(
        registerRequest, hashedPassword);

      ResponseEntity<?> response = restTemplate.postForEntity(
        "http://EMAIL/api/v1/email/send/registration-verification",
        new VerificationEmailRequestDto(verificationTokenDto.id(),
          verificationTokenDto.verificationCode().toString(), registerRequest.username(),
          registerRequest.email()),
        Void.class);

      if (!response.getStatusCode().is2xxSuccessful()) {
        throw new RestClientException("Email service failed to send verification email");
      }
    } catch (Exception e) {
      if (verificationTokenDto != null && verificationTokenDto.id() != null) {
        verificationTokenService.deleteVerificationToken(verificationTokenDto.id());
      }
      throw e;
    }
  }

  private void verifyAccountDoesNotExist(String email) {
    Optional<UserAccount> existingAccount = accountDao.findOneByEmailAndAccountType(
      email, AccountType.LOCAL);
    if (existingAccount.isPresent()) {
      throw new AccountAlreadyExistsException();
    }
  }

  @Transactional(rollbackFor = Exception.class)
  public void registerLocalAccount(VerificationTokenDto verificationTokenDto) {
    LocalRegistrationToken token = verificationTokenService.getLocalRegistrationToken(
      verificationTokenDto);
    verificationTokenService.validateVerificationToken(verificationTokenDto, token);
    ApplicationUser applicationUser = createOrReadApplicationUser(
      token.getEmail(),
      token.getUsername());
    createUserAccount(token, applicationUser);
    verificationTokenService.deleteVerificationToken(token.getId());
  }

  private ApplicationUser createOrReadApplicationUser(
    String email, String username) {
    Set<UserAccount> accountsWithMatchingEmail = accountDao.findAllByEmail(email);
    if (accountsWithMatchingEmail.isEmpty()) {
      return applicationUserDao.save(new ApplicationUser(username));
    } else if (accountsWithMatchingEmail.stream().anyMatch(
      account -> account.getAccountType().equals(AccountType.LOCAL))) {
      throw new AccountAlreadyExistsException();
    } else {
      return accountsWithMatchingEmail.stream().toList().getFirst().getApplicationUser();
    }
  }

  private void createUserAccount(LocalRegistrationToken token, ApplicationUser applicationUser) {
    UserAccount newAccount = new LocalUserAccount(token.getEmail(), token.getPassword());
    newAccount.setApplicationUser(applicationUser);
    accountDao.save(newAccount);
  }

  public LoginResponseDto loginLocalAccount(LoginRequestDto loginRequest) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
    UserAccount account = accountDao.findOneByEmailAndAccountType(
      loginRequest.email(), AccountType.LOCAL).orElseThrow(() -> new InvalidCredentialsException());
    ApplicationUser user = account.getApplicationUser();
    String accessToken = jwtService.generateAccessToken(new UserInfoDto(
      user.getId(), user.getUsername(), account.getEmail(), account.getAccountType(),
      user.getGlobalRoles()));
    return new LoginResponseDto(
      accessToken,
      new UserInfoDto(
        user.getId(), user.getUsername(), account.getEmail(), account.getAccountType(),
        user.getGlobalRoles()));
  }
}

