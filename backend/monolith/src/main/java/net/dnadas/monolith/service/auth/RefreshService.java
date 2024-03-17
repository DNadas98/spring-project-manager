package net.dnadas.monolith.service.auth;


import lombok.AllArgsConstructor;
import net.dnadas.monolith.dto.auth.RefreshRequestDto;
import net.dnadas.monolith.dto.auth.RefreshResponseDto;
import net.dnadas.monolith.dto.auth.TokenPayloadDto;
import net.dnadas.monolith.dto.auth.UserInfoDto;
import net.dnadas.monolith.exception.auth.UnauthorizedException;
import net.dnadas.monolith.model.auth.account.UserAccount;
import net.dnadas.monolith.model.auth.account.UserAccountDao;
import net.dnadas.monolith.model.user.ApplicationUser;
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

