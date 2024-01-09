package com.codecool.tasx.config.auth;

import com.codecool.tasx.service.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * {@inheritDoc}
 * - Parses JWT access token from Authorization header<br/>
 * - Extracts subject (email) from token<br/>
 * - Reads {@link UserDetails} from database<br/>
 * - Verifies JWT (expiration, signature)<br/>
 * - Cross-references it's payload with {@link UserDetails}<br/>
 * - Creates internal {@link UsernamePasswordAuthenticationToken} from {@link UserDetails}
 * and adds it to the {@link SecurityContextHolder}<br/>
 *
 * @Expiration: Appends <code>isAccessTokenExpired: true</code> to the response object if
 * the token is expired
 * @see JwtService
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final Logger logger;

  @Autowired
  public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
    this.logger = LoggerFactory.getLogger(this.getClass());
  }

  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
    @NonNull FilterChain filterChain) throws IOException {
    try {
      logger.info(request.getRequestURI());
      final String authHeader = request.getHeader("Authorization");
      final String accessTokenString;
      final String userEmail;

      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        filterChain.doFilter(request, response);
        return;
      }

      accessTokenString = authHeader.split(" ")[1];

      if (jwtService.isAccessTokenExpired(accessTokenString)) {
        logger.error("Access Token is expired");
        sendUnauthorizedResponse(response);
        response.getWriter().write("{\"isAccessTokenExpired\": true}");
        return;
      }

      userEmail = jwtService.extractSubjectFromAccessToken(accessTokenString);
      if (userEmail == null) {
        logger.error("Access Token is invalid");
        sendUnauthorizedResponse(response);
        return;
      }

      if (SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

        if (!jwtService.isAccessTokenValid(accessTokenString, userDetails)) {
          logger.error("Access Token is invalid");
          sendUnauthorizedResponse(response);
          return;
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
          userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      logger.error(e.getMessage());
      sendUnauthorizedResponse(response);
    }
  }

  private void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write("{\"error\":\"Unauthorized\"}");
  }
}
