package net.dnadas.monolith.auth.service.authentication;


import lombok.AllArgsConstructor;
import net.dnadas.monolith.auth.dto.authentication.RefreshRequestDto;
import net.dnadas.monolith.auth.dto.authentication.RefreshResponseDto;
import net.dnadas.monolith.auth.dto.authentication.TokenPayloadDto;
import net.dnadas.monolith.auth.dto.authentication.UserInfoDto;
import net.dnadas.monolith.auth.exception.authentication.UnauthorizedException;
import net.dnadas.monolith.auth.model.account.UserAccount;
import net.dnadas.monolith.auth.model.account.UserAccountDao;
import net.dnadas.monolith.auth.service.JwtService;
import net.dnadas.monolith.auth.model.user.ApplicationUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RefreshService {
  private final UserAccountDao accountDao;
  private final JwtService jwtService;

  public String getNewRefreshToken(TokenPayloadDto payloadDto) {
    return jwtService.generateRefreshToken(payloadDto);
  }

  @Transactional(readOnly = true)
  public RefreshResponseDto refresh(RefreshRequestDto refreshRequest) {
    String refreshToken = refreshRequest.refreshToken();
    TokenPayloadDto payload = jwtService.verifyRefreshToken(refreshToken);
    UserAccount account = accountDao.findOneByEmailAndAccountType(
      payload.email(), payload.accountType()).orElseThrow(() -> new UnauthorizedException());
    ApplicationUser user = account.getApplicationUser();
    String accessToken = jwtService.generateAccessToken(payload);
    return new RefreshResponseDto(
      accessToken,
      new UserInfoDto(user.getUsername(), account.getEmail(), account.getAccountType(),
        user.getGlobalRoles()));
  }
}

