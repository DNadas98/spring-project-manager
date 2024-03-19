package net.dnadas.auth.filter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.dnadas.auth.dto.authentication.TokenPayloadDto;
import net.dnadas.auth.dto.authentication.UserInfoDto;
import net.dnadas.auth.exception.authentication.UnauthorizedException;
import net.dnadas.auth.model.account.UserAccount;
import net.dnadas.auth.model.account.UserAccountDao;
import net.dnadas.auth.model.user.ApplicationUser;
import net.dnadas.auth.model.user.GlobalRole;
import net.dnadas.auth.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 * - Parses JWT access token from Authorization header<br/>
 * - Extracts subject ({@link TokenPayloadDto}) from token<br/>
 * - Reads {@link UserAccount} from database<br/>
 * - Creates internal {@link UsernamePasswordAuthenticationToken} from {@link UserAccount}
 * and adds it to the {@link SecurityContextHolder}<br/>
 *
 * @Expiration: Appends <code>isAccessTokenExpired: true</code> to the response object if
 * the token is expired
 * @see JwtService
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final UserAccountDao accountDao;
  private final JwtService jwtService;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
    @NonNull FilterChain filterChain) throws IOException, ServletException {
    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      filterChain.doFilter(request, response);
      return;
    }
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    String accessToken = authHeader.split(" ")[1];

    try {
      if (jwtService.isAccessTokenExpired(accessToken)) {
        logger.error("Access Token is expired");
        setAccessTokenExpired(response);
        return;
      }

      UserInfoDto userInfoDto = jwtService.verifyAccessToken(accessToken);
      UserAccount account = accountDao.findOneByEmailAndAccountType(
        userInfoDto.email(), userInfoDto.accountType()).orElseThrow(
        () -> new UnauthorizedException());

      //TODO: implement -- USER INFO MIGHT CHANGE

      ApplicationUser applicationUser = account.getApplicationUser();

      SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
        new UserInfoDto(applicationUser.getId(), applicationUser.getUsername(), account.getEmail(),
          account.getAccountType(), applicationUser.getGlobalRoles()), null,
        getAuthorities(applicationUser.getGlobalRoles())));
      filterChain.doFilter(request, response);
    } catch (JwtException | UnauthorizedException e) {
      logger.error(e.getMessage());
      setUnauthorizedResponse(response);
    }
  }

  private Set<SimpleGrantedAuthority> getAuthorities(Set<GlobalRole> globalRoles) {
    return globalRoles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(
      Collectors.toSet());
  }

  private void setAccessTokenExpired(HttpServletResponse response) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write("{\"error\":\"Unauthorized\", \"isAccessTokenExpired\": true}");
  }

  private void setUnauthorizedResponse(HttpServletResponse response) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write("{\"error\":\"Unauthorized\"}");
  }
}
