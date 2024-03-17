package net.dnadas.monolith.service.auth;


import lombok.AllArgsConstructor;
import net.dnadas.monolith.dto.auth.*;
import net.dnadas.monolith.dto.email.EmailRequestDto;
import net.dnadas.monolith.dto.verification.VerificationTokenDto;
import net.dnadas.monolith.exception.auth.AccountAlreadyExistsException;
import net.dnadas.monolith.exception.auth.InvalidCredentialsException;
import net.dnadas.monolith.model.auth.account.AccountType;
import net.dnadas.monolith.model.auth.account.LocalUserAccount;
import net.dnadas.monolith.model.auth.account.UserAccount;
import net.dnadas.monolith.model.auth.account.UserAccountDao;
import net.dnadas.monolith.model.user.ApplicationUser;
import net.dnadas.monolith.model.user.ApplicationUserDao;
import net.dnadas.monolith.model.verification.LocalRegistrationToken;
import net.dnadas.monolith.service.email.EmailService;
import net.dnadas.monolith.service.email.EmailTemplateService;
import net.dnadas.monolith.service.verification.VerificationTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  private final EmailService emailService;
  private final EmailTemplateService emailTemplateService;

  @Transactional(rollbackFor = Exception.class)
  public void sendRegistrationVerificationEmail(RegisterRequestDto registerRequest)
    throws Exception {
    VerificationTokenDto verificationTokenDto = null;
    try {
      verifyAccountDoesNotExist(registerRequest.email());
      verificationTokenService.verifyLocalRegistrationTokenDoesNotExist(registerRequest);
      String hashedPassword = passwordEncoder.encode(registerRequest.password());
      verificationTokenDto = verificationTokenService.saveLocalRegistrationToken(
        registerRequest, hashedPassword);
      EmailRequestDto emailRequestDto = emailTemplateService.getRegistrationEmailDto(
        verificationTokenDto, registerRequest.email(), registerRequest.username());
      emailService.sendMailToUserAddress(emailRequestDto);
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
    TokenPayloadDto payloadDto = new TokenPayloadDto(account.getEmail(), account.getAccountType());
    String accessToken = jwtService.generateAccessToken(payloadDto);
    return new LoginResponseDto(
      accessToken,
      new UserInfoDto(user.getUsername(), account.getEmail(), account.getAccountType(),
        user.getGlobalRoles()));
  }
}

