package net.dnadas.auth.service.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.dnadas.auth.dto.authentication.UserInfoDto;
import net.dnadas.auth.exception.oauth2.OAuth2ProcessingException;
import net.dnadas.auth.model.account.OAuth2UserAccount;
import net.dnadas.auth.service.CookieService;
import net.dnadas.auth.service.authentication.RefreshService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final AuthorizationRequestRepository<OAuth2AuthorizationRequest> requestService;
  private final RefreshService refreshService;
  private final CookieService cookieService;

  @Value("${BACKEND_OAUTH2_FRONTEND_REDIRECT_URI}")
  private String FRONTEND_REDIRECT_URI;


  @Override
  @Transactional(readOnly = true)
  public void onAuthenticationSuccess(
    HttpServletRequest request, HttpServletResponse response, Authentication authentication)
    throws IOException, OAuth2ProcessingException {
    if (response.isCommitted()) {
      throw new OAuth2ProcessingException(
        "Unable to redirect to " + FRONTEND_REDIRECT_URI);
    }
    requestService.removeAuthorizationRequest(request, response);
    OAuth2UserAccount userAccount = getAccount(authentication);
    String refreshToken = refreshService.getNewRefreshToken(new UserInfoDto(
      userAccount.getApplicationUser().getId(), userAccount.getApplicationUser().getUsername(),
      userAccount.getEmail(), userAccount.getAccountType(),
      userAccount.getApplicationUser().getGlobalRoles()
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
