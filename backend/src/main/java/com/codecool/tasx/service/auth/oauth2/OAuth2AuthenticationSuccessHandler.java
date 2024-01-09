package com.codecool.tasx.service.auth.oauth2;

import com.codecool.tasx.exception.auth.OAuth2ProcessingException;
import com.codecool.tasx.model.user.User;
import com.codecool.tasx.service.auth.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final HttpCookieOAuth2AuthorizationRequestRepository
    httpCookieOAuth2AuthorizationRequestRepository;
  private final JwtService jwtService;
  private final CookieService cookieService;

  @Value("${BACKEND_OAUTH2_FRONTEND_REDIRECT_URI}")
  private String FRONTEND_REDIRECT_URI;

  @Autowired
  public OAuth2AuthenticationSuccessHandler(
    HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository,
    JwtService jwtService, CookieService cookieService) {
    this.httpCookieOAuth2AuthorizationRequestRepository =
      httpCookieOAuth2AuthorizationRequestRepository;
    this.jwtService = jwtService;
    this.cookieService = cookieService;
  }


  @Override
  public void onAuthenticationSuccess(
    HttpServletRequest request, HttpServletResponse response, Authentication authentication)
    throws IOException, OAuth2ProcessingException {
    if (response.isCommitted()) {
      throw new OAuth2ProcessingException(
        "Response has already been committed. Unable to redirect to " + FRONTEND_REDIRECT_URI);
    }
    httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(
      request,
      response);
    User user = getUser(authentication);
    String refreshToken = jwtService.generateRefreshToken(user);
    cookieService.addRefreshCookie(refreshToken, response);
    super.getRedirectStrategy().sendRedirect(request, response, FRONTEND_REDIRECT_URI);
  }


  private User getUser(Authentication authentication) throws OAuth2ProcessingException {
    User user;
    try {
      user = (User) authentication.getPrincipal();
      return user;
    } catch (Exception e) {
      throw new OAuth2ProcessingException("Failed to parse user from context");
    }
  }
}
