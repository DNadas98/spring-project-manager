package com.codecool.tasx.config.auth;

import com.codecool.tasx.service.auth.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityFilterChainConfig {
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final AuthenticationProvider authenticationProvider;
  private final HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository;
  private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;
  private final AuthenticationSuccessHandler authenticationSuccessHandler;
  private final AuthenticationFailureHandler authenticationFailureHandler;

  @Autowired
  public SecurityFilterChainConfig(
    JwtAuthenticationFilter jwtAuthenticationFilter,
    AuthenticationProvider authenticationProvider,
    HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository,
    OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService,
    AuthenticationSuccessHandler authenticationSuccessHandler,
    AuthenticationFailureHandler authenticationFailureHandler) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.authenticationProvider = authenticationProvider;
    this.authorizationRequestRepository = authorizationRequestRepository;
    this.oAuth2UserService = oAuth2UserService;
    this.authenticationSuccessHandler = authenticationSuccessHandler;
    this.authenticationFailureHandler = authenticationFailureHandler;
  }

  /**
   * {@inheritDoc}
   *
   * @CSRF: disabled
   * @White-list: "/api/v1/auth/**", "/error" (access allowed without authentication)
   * @Session: Stateless (no {@link jakarta.servlet.http.HttpSession})
   * @Provider: Use the {@link AuthenticationProvider} defined in {@link SecurityConfig}
   * @Authentication: Use {@link JwtAuthenticationFilter} for all non-whitelisted endpoints
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
      .authorizeHttpRequests(authorizeRequestsConfigurer -> authorizeRequestsConfigurer
        .requestMatchers(
          "/error",
          "/favicon.ico")
        .permitAll()
        .requestMatchers(
          "/api/v1/auth/**",
          "/oauth2/**")
        .permitAll()
        .anyRequest().authenticated())
      .csrf(AbstractHttpConfigurer::disable)
      .formLogin(AbstractHttpConfigurer::disable)
      .httpBasic(AbstractHttpConfigurer::disable)
      .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authenticationProvider(authenticationProvider)
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
      .oauth2Login(configurer -> configurer
        .authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig
          .baseUri("/oauth2/authorize")
          .authorizationRequestRepository(authorizationRequestRepository))
        .redirectionEndpoint(redirectionEndpointConfig -> redirectionEndpointConfig
          .baseUri("/oauth2/callback/*"))
        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
          .userService(oAuth2UserService))
        .successHandler(authenticationSuccessHandler)
        .failureHandler(authenticationFailureHandler)
      );

    return httpSecurity.build();
  }
}
