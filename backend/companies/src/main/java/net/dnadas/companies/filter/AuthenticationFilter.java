package net.dnadas.companies.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.dnadas.auth.dto.authentication.UserInfoDto;
import net.dnadas.auth.exception.authentication.UnauthorizedException;
import net.dnadas.auth.model.user.GlobalRole;
import net.dnadas.companies.service.authentication.JwtVerificationService;
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

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final JwtVerificationService jwtVerificationService;

  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
    @NonNull FilterChain filterChain) throws IOException, ServletException {
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    String token = authHeader.substring(7);

    try {
      UserInfoDto userInfoDto = jwtVerificationService.verifyAccessToken(token);

      SecurityContextHolder.getContext().setAuthentication(getAuthenticationToken(userInfoDto));
      filterChain.doFilter(request, response);
    } catch (UnauthorizedException e) {
      logger.error(e.getMessage());
      setUnauthorizedResponse(response);
    }
  }

  private UsernamePasswordAuthenticationToken getAuthenticationToken(UserInfoDto userInfoDto) {
    return new UsernamePasswordAuthenticationToken(
      userInfoDto, null,
      getAuthorities(userInfoDto.roles()));
  }

  private Set<SimpleGrantedAuthority> getAuthorities(Set<GlobalRole> globalRoles) {
    return globalRoles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(
      Collectors.toSet());
  }

  private void setUnauthorizedResponse(HttpServletResponse response) throws IOException {
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write("{\"error\":\"Unauthorized\"}");
  }
}
