package com.codecool.tasx.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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

  public String generateAccessToken(UserDetails userDetails) {
    return generateToken(
      new HashMap<>(), userDetails, accessTokenExpiration, accessTokenSecret, accessTokenAlgorithm);
  }

  public String generateRefreshToken(UserDetails userDetails) {
    return generateToken(
      new HashMap<>(), userDetails, refreshTokenExpiration, refreshTokenSecret,
      refreshTokenAlgorithm);
  }

  public boolean isAccessTokenValid(String accessToken, UserDetails userDetails) {
    return isTokenValid(accessToken, userDetails, accessTokenSecret, accessTokenAlgorithm);
  }

  public boolean isAccessTokenExpired(String accessToken) {
    return isTokenExpired(accessToken, accessTokenSecret, accessTokenAlgorithm);
  }

  public boolean isRefreshTokenValid(String refreshToken, UserDetails userDetails) {
    return isTokenValid(refreshToken, userDetails, refreshTokenSecret, refreshTokenAlgorithm);
  }

  private String generateToken(
    Map<String, Object> extraClaims, UserDetails userDetails,
    Long expiration, String secret, SignatureAlgorithm algorithm) {
    Date now = new Date();
    Date expirationDate = new Date(now.getTime() + expiration);

    return Jwts.builder()
      .setClaims(extraClaims)
      .setSubject(userDetails.getUsername())
      .setIssuedAt(now)
      .setExpiration(expirationDate)
      .signWith(getSigningKey(secret), algorithm)
      .compact();
  }

  private Key getSigningKey(String secret) {
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private boolean isTokenValid(
    String token, UserDetails userDetails, String secret, SignatureAlgorithm algorithm) {
    final String subject = extractSubjectFromToken(token, secret, algorithm);
    return (subject.equals(userDetails.getUsername())) && !isTokenExpired(
      token, secret, algorithm);
  }

  public String extractSubjectFromAccessToken(String accessToken) {
    return extractClaimFromToken(
      accessToken, accessTokenSecret, accessTokenAlgorithm, Claims::getSubject);
  }

  public String extractSubjectFromRefreshToken(String refreshToken) {
    return extractClaimFromToken(
      refreshToken, refreshTokenSecret, refreshTokenAlgorithm, Claims::getSubject);
  }

  private String extractSubjectFromToken(
    String token, String secret, SignatureAlgorithm algorithm) {
    return extractClaimFromToken(token, secret, algorithm, Claims::getSubject);
  }

  private boolean isTokenExpired(String token, String secret, SignatureAlgorithm algorithm) {
    return extractExpirationFromToken(token, secret, algorithm).before(new Date());
  }

  private <T> T extractClaimFromToken(
    String token, String secret, SignatureAlgorithm algorithm, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaimsFromToken(token, secret, algorithm);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaimsFromToken(
    String token, String secret, SignatureAlgorithm algorithm) {
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

