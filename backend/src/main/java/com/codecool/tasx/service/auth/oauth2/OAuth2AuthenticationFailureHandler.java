package com.codecool.tasx.service.auth.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  private final HttpCookieOAuth2AuthorizationRequestRepository
    httpCookieOAuth2AuthorizationRequestRepository;
  private final CookieService cookieService;

  @Value("${BACKEND_OAUTH2_FRONTEND_REDIRECT_URI}")
  private String FRONTEND_REDIRECT_URI;

  @Autowired
  public OAuth2AuthenticationFailureHandler(
    HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository,
    CookieService cookieService) {
    this.httpCookieOAuth2AuthorizationRequestRepository =
      httpCookieOAuth2AuthorizationRequestRepository;
    this.cookieService = cookieService;
  }

  @Override
  public void onAuthenticationFailure(
    HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
    throws IOException {
    /*String targetUrl = CookieService.getCookie(
        request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
      .map(Cookie::getValue)
      .orElse(("/"));
    targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
      .queryParam("error", exception.getLocalizedMessage())
      .build().toUriString();*/

    httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(
      request, response);

    cookieService.clearRefreshCookie(response);

    getRedirectStrategy().sendRedirect(request, response, FRONTEND_REDIRECT_URI);
  }
}
