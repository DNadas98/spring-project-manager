package net.dnadas.auth.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import net.dnadas.auth.dto.authentication.UserInfoDto;
import net.dnadas.auth.exception.authentication.UnauthorizedException;
import net.dnadas.auth.model.account.AccountType;
import net.dnadas.auth.model.user.GlobalRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

  private final String accessTokenSecret;
  private final Long accessTokenExpiration;
  private final SignatureAlgorithm accessTokenAlgorithm;

  private final String refreshTokenSecret;
  private final Long refreshTokenExpiration;
  private final SignatureAlgorithm refreshTokenAlgorithm;

  public JwtService(
    @Value("${BACKEND_ACCESS_TOKEN_SECRET}") String accessTokenSecret,
    @Value("${BACKEND_ACCESS_TOKEN_EXPIRATION}") Long accessTokenExpiration,
    @Value("${BACKEND_REFRESH_TOKEN_SECRET}") String refreshTokenSecret,
    @Value("${BACKEND_REFRESH_TOKEN_EXPIRATION}") Long refreshTokenExpiration) {
    this.accessTokenSecret = accessTokenSecret;
    this.accessTokenExpiration = accessTokenExpiration;
    accessTokenAlgorithm = SignatureAlgorithm.HS256;
    this.refreshTokenSecret = refreshTokenSecret;
    this.refreshTokenExpiration = refreshTokenExpiration;
    refreshTokenAlgorithm = SignatureAlgorithm.HS256;
  }

  public String generateAccessToken(UserInfoDto payloadDto) {
    return generateToken(
      payloadDto, accessTokenExpiration, accessTokenSecret, accessTokenAlgorithm);
  }

  public String generateRefreshToken(UserInfoDto payloadDto) {
    return generateToken(payloadDto, refreshTokenExpiration, refreshTokenSecret,
      refreshTokenAlgorithm);
  }

  public boolean isAccessTokenExpired(String accessToken) {
    try {
      return isTokenExpired(accessToken, accessTokenSecret, accessTokenAlgorithm);
    } catch (JwtException e) {
      throw new UnauthorizedException();
    }
  }

  public UserInfoDto verifyAccessToken(String accessToken) {
    try {
      Claims claims = extractAllClaimsFromToken(
        accessToken, accessTokenSecret);
      return getPayloadDto(claims);
    } catch (JwtException e) {
      throw new UnauthorizedException();
    }
  }

  public UserInfoDto verifyRefreshToken(String refreshToken) {
    try {
      Claims claims = extractAllClaimsFromToken(refreshToken, refreshTokenSecret);
      return getPayloadDto(claims);
    } catch (JwtException e) {
      throw new UnauthorizedException();
    }
  }

  private String generateToken(
    UserInfoDto userInfoDto,
    Long expiration, String secret, SignatureAlgorithm algorithm) {
    Date now = new Date();
    Date expirationDate = new Date(now.getTime() + expiration);
    Map claims = new HashMap();
    claims.put("userId", userInfoDto.userId());
    claims.put("username", userInfoDto.username());
    claims.put("accountType", userInfoDto.accountType().name());
    claims.put(
      "roles", userInfoDto.roles().stream().map(Enum::name).collect(Collectors.joining(",")));

    return Jwts.builder()
      .setClaims(claims)
      .setSubject(userInfoDto.email())
      .setIssuedAt(now)
      .setExpiration(expirationDate)
      .signWith(getSigningKey(secret), algorithm)
      .compact();
  }

  private UserInfoDto getPayloadDto(Claims claims) {
    try {
      String email = claims.getSubject();
      Long userId = Long.parseLong(claims.get("userId").toString());
      String username = claims.get("username").toString();
      AccountType accountType = AccountType.valueOf(claims.get("accountType").toString());
      Set<GlobalRole> roles = Arrays.stream(claims.get("roles").toString().split(","))
        .map(GlobalRole::valueOf)
        .collect(Collectors.toSet());
      return new UserInfoDto(userId, username, email, accountType, roles);
    } catch (IllegalArgumentException | NullPointerException e) {
      throw new UnauthorizedException();
    }
  }

  private Key getSigningKey(String secret) {
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private boolean isTokenExpired(String token, String secret, SignatureAlgorithm algorithm) {
    try {
      return extractExpirationFromToken(token, secret, algorithm).before(new Date());
    } catch (ExpiredJwtException e) {
      return true;
    }
  }

  private <T> T extractClaimFromToken(
    String token, String secret, SignatureAlgorithm algorithm, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaimsFromToken(token, secret);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaimsFromToken(
    String token, String secret) {
    return Jwts.parserBuilder()
      .setSigningKey(getSigningKey(secret))
      .build()
      .parseClaimsJws(token)
      .getBody();
  }

  private Date extractExpirationFromToken(
    String token, String secret, SignatureAlgorithm algorithm) {
    return extractClaimFromToken(token, secret, algorithm, Claims::getExpiration);
  }
}

