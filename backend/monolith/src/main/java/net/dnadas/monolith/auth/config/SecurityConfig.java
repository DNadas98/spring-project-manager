package net.dnadas.monolith.auth.config;

import lombok.RequiredArgsConstructor;
import net.dnadas.monolith.auth.filter.JwtAuthenticationFilter;
import net.dnadas.monolith.auth.model.oauth2.OAuth2AuthorizationRequestDao;
import net.dnadas.monolith.auth.model.account.AccountType;
import net.dnadas.monolith.auth.model.account.UserAccountDao;
import net.dnadas.monolith.model.company.CompanyDao;
import net.dnadas.monolith.model.company.project.ProjectDao;
import net.dnadas.monolith.model.company.project.task.TaskDao;
import net.dnadas.monolith.auth.model.user.ApplicationUserDao;
import net.dnadas.monolith.auth.model.user.GlobalRole;
import net.dnadas.monolith.auth.service.CookieService;
import net.dnadas.monolith.auth.service.authorization.CustomPermissionEvaluator;
import net.dnadas.monolith.auth.service.authentication.RefreshService;
import net.dnadas.monolith.auth.service.oauth2.DatabaseOAuth2AuthorizationRequestService;
import net.dnadas.monolith.auth.service.oauth2.OAuth2AuthenticationFailureHandler;
import net.dnadas.monolith.auth.service.oauth2.OAuth2AuthenticationSuccessHandler;
import net.dnadas.monolith.auth.service.oauth2.OAuth2UserAccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
  private final UserAccountDao userAccountDao;
  private final ApplicationUserDao applicationUserDao;
  private final CompanyDao companyDao;
  private final ProjectDao projectDao;
  private final TaskDao taskDao;
  private final RefreshService refreshService;
  private final CookieService cookieService;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final OAuth2AuthorizationRequestDao oAuth2AuthorizationRequestDao;

  /**
   * Unique field of {@link UserDetailsService} that represents the subject is always called
   * "username", in our application it is the email.
   */
  @Bean
  public UserDetailsService userDetailsService() {
    return username -> (UserDetails) userAccountDao.findOneByEmailAndAccountType(
      username, AccountType.LOCAL).orElseThrow(() -> new UsernameNotFoundException(
      String.format(
        "%s account not found with the provided e-mail address",
        AccountType.LOCAL.getDisplayName())));
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

  // OAuth2

  @Bean
  public AuthorizationRequestRepository<OAuth2AuthorizationRequest> requestRepository() {
    return new DatabaseOAuth2AuthorizationRequestService(oAuth2AuthorizationRequestDao);
  }

  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return new OAuth2AuthenticationSuccessHandler(
      requestRepository(), refreshService, cookieService);
  }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler() {
    return new OAuth2AuthenticationFailureHandler(requestRepository());
  }

  @Bean
  public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
    return new OAuth2UserAccountService(userAccountDao, applicationUserDao);
  }

  // Method-level security

  @Bean
  public MethodSecurityExpressionHandler expressionHandler() {
    var expressionHandler = new DefaultMethodSecurityExpressionHandler();
    expressionHandler.setPermissionEvaluator(
      new CustomPermissionEvaluator(applicationUserDao, companyDao, projectDao, taskDao));
    return expressionHandler;
  }

  @Bean
  @Order(1)
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
      .securityMatcher("/api/v1/**")
      .authorizeHttpRequests(authorizeRequestsConfigurer -> authorizeRequestsConfigurer
        .requestMatchers("/api/v1/admin", "/api/v1/admin/**").hasAuthority(GlobalRole.ADMIN.name())
        .requestMatchers("/api/v1/user", "/api/v1/user/**", "/api/v1/companies",
          "/api/v1/companies/**").hasAuthority(GlobalRole.USER.name())
        .anyRequest().permitAll())
      .csrf(AbstractHttpConfigurer::disable)
      .formLogin(AbstractHttpConfigurer::disable)
      .httpBasic(AbstractHttpConfigurer::disable)
      .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authenticationProvider(authenticationProvider())
      .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
      .build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
      .securityMatcher("/oauth2/**")
      .csrf(AbstractHttpConfigurer::disable)
      .formLogin(AbstractHttpConfigurer::disable)
      .httpBasic(AbstractHttpConfigurer::disable)
      .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authenticationProvider(authenticationProvider())
      .oauth2Login(configurer -> configurer
        .authorizationEndpoint(authorizationEndpointConfig -> authorizationEndpointConfig
          .baseUri("/oauth2/authorize")
          .authorizationRequestRepository(requestRepository()))
        .redirectionEndpoint(redirectionEndpointConfig -> redirectionEndpointConfig
          .baseUri("/oauth2/callback/*"))
        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
          .userService(oAuth2UserService()))
        .successHandler(authenticationSuccessHandler())
        .failureHandler(authenticationFailureHandler())
      ).build();
  }
}