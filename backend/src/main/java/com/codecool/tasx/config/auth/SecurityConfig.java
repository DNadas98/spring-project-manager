package com.codecool.tasx.config.auth;

import com.codecool.tasx.model.user.UserDao;
import com.codecool.tasx.service.auth.JwtService;
import com.codecool.tasx.service.auth.oauth2.CookieService;
import com.codecool.tasx.service.auth.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.codecool.tasx.service.auth.oauth2.OAuth2AuthenticationFailureHandler;
import com.codecool.tasx.service.auth.oauth2.OAuth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private final UserDao userDao;
  private final JwtService jwtService;
  private final CookieService cookieService;
  private final HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository;


  @Autowired
  public SecurityConfig(
    UserDao userDao, JwtService jwtService, CookieService cookieService,
    HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository) {
    this.userDao = userDao;
    this.jwtService = jwtService;
    this.cookieService = cookieService;
    this.cookieAuthorizationRequestRepository = cookieAuthorizationRequestRepository;
  }

  /**
   * Unique field of {@link UserDetailsService} that represents the subject is always called
   * "username", in our application it is the email.
   */
  @Bean
  public UserDetailsService userDetailsService() {
    return username -> userDao.findOneByEmail(username).orElseThrow(
      () -> new UsernameNotFoundException("User account not found"));
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(userDetailsService());
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
    return daoAuthenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
    throws Exception {
    return config.getAuthenticationManager();
  }

  //OAuth2

  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return new OAuth2AuthenticationSuccessHandler(
      cookieAuthorizationRequestRepository, jwtService, cookieService);
  }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler() {
    return new OAuth2AuthenticationFailureHandler(
      cookieAuthorizationRequestRepository,
      cookieService);
  }
}