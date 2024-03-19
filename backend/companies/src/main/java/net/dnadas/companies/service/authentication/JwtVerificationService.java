package net.dnadas.companies.service.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import net.dnadas.auth.dto.authentication.UserInfoDto;
import net.dnadas.auth.exception.authentication.UnauthorizedException;
import net.dnadas.auth.model.account.AccountType;
import net.dnadas.auth.model.user.GlobalRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtVerificationService {

  private final String accessTokenSecret;
  private final SignatureAlgorithm accessTokenAlgorithm = SignatureAlgorithm.HS256;

  public JwtVerificationService(@Value("${BACKEND_ACCESS_TOKEN_SECRET}") String accessTokenSecret) {
    this.accessTokenSecret = accessTokenSecret;
  }

  public UserInfoDto verifyAccessToken(String accessToken) throws UnauthorizedException {
    try {
      Claims claims = extractAllClaimsFromToken(accessToken);
      return getPayloadDto(claims);
    } catch (Exception e) {
      throw new UnauthorizedException("Invalid or expired access token.");
    }
  }

  private UserInfoDto getPayloadDto(Claims claims) {
    String email = claims.getSubject();
    String username = claims.get("username").toString();
    AccountType accountType = AccountType.valueOf(claims.get("accountType").toString());
    Long userId = Long.parseLong(claims.get("userId").toString());
    Set<GlobalRole> roles = Arrays.stream(claims.get("roles").toString().split(","))
      .map(GlobalRole::valueOf)
      .collect(Collectors.toSet());
    return new UserInfoDto(userId, username, email, accountType, roles);
  }

  private Claims extractAllClaimsFromToken(String token) {
    return Jwts.parserBuilder()
      .setSigningKey(getSigningKey(accessTokenSecret))
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  private Key getSigningKey(String secret) {
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}