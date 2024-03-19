package net.dnadas.gateway.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
  private final RestTemplate restTemplate;

  /**
   * Same contract as for {@code doFilter}, but guaranteed to be
   * just invoked once per request within a single request thread.
   * See {@link #shouldNotFilterAsyncDispatch()} for details.
   * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
   * default ServletRequest and ServletResponse ones.
   *
   * @param request
   * @param response
   * @param filterChain
   */
  @Override
  protected void doFilterInternal(
    HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws IOException {
    try {
      String[] authenticationPaths = {"/api/v1/auth", "/api/v1/user", "/oauth2"};
      if (Arrays.stream(authenticationPaths).anyMatch(request.getRequestURI()::startsWith)) {
        filterChain.doFilter(request, response);
        return;
      }

      HttpEntity<Void> entity = getRequestEntity(request);
      restTemplate.exchange(
        "http://AUTH/api/v1/user/authenticate", HttpMethod.GET, entity, Void.class);

      filterChain.doFilter(request, response);
    } catch (HttpClientErrorException e) {
      response.setStatus(e.getStatusCode().value());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.getWriter().write("{\"error\":\"Unauthorized\"}");
    } catch (Exception e) {
      response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.getWriter().write("{\"error\":\"Internal Server Error\"}");
    }

  }

  private HttpEntity<Void> getRequestEntity(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", authHeader);
    HttpEntity<Void> entity = new HttpEntity<>(headers);
    return entity;
  }
}
