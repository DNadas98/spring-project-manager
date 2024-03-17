package net.dnadas.monolith.auth.service.verification;

import lombok.RequiredArgsConstructor;
import net.dnadas.monolith.auth.dto.authentication.RegisterRequestDto;
import net.dnadas.monolith.auth.dto.verification.VerificationTokenDto;
import net.dnadas.monolith.auth.exception.authentication.UnauthorizedException;
import net.dnadas.monolith.auth.exception.verification.VerificationTokenAlreadyExistsException;
import net.dnadas.monolith.auth.model.verification.LocalRegistrationToken;
import net.dnadas.monolith.auth.model.verification.LocalRegistrationTokenDao;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {
  private final LocalRegistrationTokenDao localRegistrationTokenDao;
  private final PasswordEncoder tokenCodeEncoder;

  public LocalRegistrationToken getLocalRegistrationToken(
    VerificationTokenDto verificationTokenDto) {
    LocalRegistrationToken token = localRegistrationTokenDao.findById(verificationTokenDto.id())
      .orElseThrow(() -> new UnauthorizedException());
    return token;
  }

  public void validateVerificationToken(
    VerificationTokenDto verificationTokenDto, LocalRegistrationToken token) {
    if (!tokenCodeEncoder.matches(
      verificationTokenDto.verificationCode().toString(), token.getVerificationCodeHash())) {
      throw new UnauthorizedException();
    }
  }

  public void verifyLocalRegistrationTokenDoesNotExist(RegisterRequestDto registerRequest) {
    Optional<LocalRegistrationToken> existingToken = localRegistrationTokenDao.findByEmail(
      registerRequest.email());
    if (existingToken.isPresent()) {
      throw new VerificationTokenAlreadyExistsException();
    }
  }

  public void deleteVerificationToken(Long tokenId) {
    localRegistrationTokenDao.deleteById(tokenId);
  }

  public VerificationTokenDto saveLocalRegistrationToken(
    RegisterRequestDto registerRequest, String hashedPassword) {
    UUID verificationCode = UUID.randomUUID();
    String hashedVerificationCode = tokenCodeEncoder.encode(verificationCode.toString());
    LocalRegistrationToken registrationToken = new LocalRegistrationToken(
      registerRequest.email(), registerRequest.username(), hashedPassword, hashedVerificationCode);
    LocalRegistrationToken savedToken = localRegistrationTokenDao.save(registrationToken);
    return new VerificationTokenDto(savedToken.getId(), verificationCode);
  }
}
