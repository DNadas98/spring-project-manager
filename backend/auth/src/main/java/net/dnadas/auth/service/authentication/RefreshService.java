package net.dnadas.auth.service.authentication;


import lombok.AllArgsConstructor;
import net.dnadas.auth.dto.authentication.RefreshRequestDto;
import net.dnadas.auth.dto.authentication.RefreshResponseDto;
import net.dnadas.auth.dto.authentication.UserInfoDto;
import net.dnadas.auth.exception.authentication.UnauthorizedException;
import net.dnadas.auth.model.account.UserAccount;
import net.dnadas.auth.model.account.UserAccountDao;
import net.dnadas.auth.model.user.ApplicationUser;
import net.dnadas.auth.service.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RefreshService {
  private final UserAccountDao accountDao;
  private final JwtService jwtService;

  public String getNewRefreshToken(UserInfoDto payloadDto) {
    return jwtService.generateRefreshToken(payloadDto);
  }

  @Transactional(readOnly = true)
  public RefreshResponseDto refresh(RefreshRequestDto refreshRequest) {
    String refreshToken = refreshRequest.refreshToken();
    UserInfoDto userInfoDto = jwtService.verifyRefreshToken(refreshToken);
    UserAccount account = accountDao.findOneByEmailAndAccountType(
      userInfoDto.email(), userInfoDto.accountType()).orElseThrow(
      () -> new UnauthorizedException());
    ApplicationUser user = account.getApplicationUser();
    String accessToken = jwtService.generateAccessToken(userInfoDto);
    return new RefreshResponseDto(
      accessToken,
      new UserInfoDto(
        user.getId(), user.getUsername(), account.getEmail(), account.getAccountType(),
        user.getGlobalRoles()));
  }
}

