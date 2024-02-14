package com.codecool.tasx.service.auth.oauth2;

import com.codecool.tasx.controller.dto.user.auth.TokenPayloadDto;
import com.codecool.tasx.exception.auth.OAuth2ProcessingException;
import com.codecool.tasx.model.auth.account.OAuth2UserAccount;
import com.codecool.tasx.service.auth.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> requestRepository;
  private final JwtService jwtService;
  private final CookieService cookieService;

  @Value("${BACKEND_OAUTH2_FRONTEND_REDIRECT_URI}")
  private String FRONTEND_REDIRECT_URI;

  @Autowired
  public OAuth2AuthenticationSuccessHandler(
    AuthorizationRequestRepository<OAuth2AuthorizationRequest> requestRepository,
    JwtService jwtService, CookieService cookieService) {
    this.requestRepository = requestRepository;
    this.jwtService = jwtService;
    this.cookieService = cookieService;
  }


  @Override
  public void onAuthenticationSuccess(
    HttpServletRequest request, HttpServletResponse response, Authentication authentication)
    throws IOException, OAuth2ProcessingException {
    if (response.isCommitted()) {
      throw new OAuth2ProcessingException(
        "Unable to redirect to " + FRONTEND_REDIRECT_URI);
    }
    requestRepository.removeAuthorizationRequest(request, response);
    OAuth2UserAccount userAccount = getAccount(authentication);
    String refreshToken = jwtService.generateRefreshToken(new TokenPayloadDto(
      userAccount.getEmail(), userAccount.getAccountType()
    ));
    cookieService.addRefreshCookie(refreshToken, response);
    super.getRedirectStrategy().sendRedirect(request, response, FRONTEND_REDIRECT_URI);
  }


  private OAuth2UserAccount getAccount(Authentication authentication)
    throws OAuth2ProcessingException {
    try {
      return (OAuth2UserAccount) authentication.getPrincipal();
    } catch (Exception e) {
      throw new OAuth2ProcessingException("Failed to parse Application User from context");
    }
  }
}
