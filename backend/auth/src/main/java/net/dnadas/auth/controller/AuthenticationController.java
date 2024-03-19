package net.dnadas.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import net.dnadas.auth.dto.authentication.*;
import net.dnadas.auth.dto.verification.VerificationTokenDto;
import net.dnadas.auth.service.CookieService;
import net.dnadas.auth.service.account.LocalUserAccountService;
import net.dnadas.auth.service.authentication.RefreshService;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final LocalUserAccountService localUserAccountService;
  private final RefreshService refreshService;
  private final CookieService cookieService;


  @PostMapping("/register")
  public ResponseEntity<?> register(
    @RequestBody @Valid RegisterRequestDto request) throws Exception {
    localUserAccountService.sendRegistrationVerificationEmail(
      request);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of(
      "message",
      "Registration process started successfully, e-mail verification is required to proceed"));
  }

  @PostMapping("/verify-registration")
  public ResponseEntity<?> verifyRegistration(
    @RequestParam(name = "code") UUID verificationCode,
    @RequestParam(name = "id") @Min(1) Long verificationTokenId) {
    localUserAccountService.registerLocalAccount(
      new @Valid VerificationTokenDto(verificationTokenId, verificationCode));
    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
      "message",
      "Local account registered successfully, sign in to proceed"));
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(
    @RequestBody @Valid LoginRequestDto loginRequest, HttpServletResponse response) {
    LoginResponseDto loginResponse = localUserAccountService.loginLocalAccount(loginRequest);

    String refreshToken = refreshService.getNewRefreshToken(
      loginResponse.userInfo());
    cookieService.addRefreshCookie(refreshToken, response);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", loginResponse));
  }

  @GetMapping("/refresh")
  public ResponseEntity<?> refresh(@CookieValue(name = "jwt") @Length(min = 1) String jwt) {
    RefreshResponseDto refreshResponse = refreshService.refresh(new RefreshRequestDto(jwt));
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", refreshResponse));
  }

  @GetMapping("/logout")
  public ResponseEntity<?> logout(
    @CookieValue(required = false) String jwt, HttpServletResponse response) {
    if (jwt == null) {
      return ResponseEntity.noContent().build();
    }
    cookieService.clearRefreshCookie(response);
    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Account logged out successfully"));
  }
}
