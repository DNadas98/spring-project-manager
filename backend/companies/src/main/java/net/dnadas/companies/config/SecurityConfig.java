package net.dnadas.companies.config;

import lombok.RequiredArgsConstructor;
import net.dnadas.auth.model.user.GlobalRole;
import net.dnadas.companies.filter.AuthenticationFilter;
import net.dnadas.companies.model.company.CompanyDao;
import net.dnadas.companies.model.company.project.ProjectDao;
import net.dnadas.companies.model.company.project.task.TaskDao;
import net.dnadas.companies.service.authorization.CustomPermissionEvaluator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
  private final CompanyDao companyDao;
  private final ProjectDao projectDao;
  private final TaskDao taskDao;
  private final AuthenticationFilter authenticationFilter;

  // Method-level security
  @Bean
  public MethodSecurityExpressionHandler expressionHandler() {
    var expressionHandler = new DefaultMethodSecurityExpressionHandler();
    expressionHandler.setPermissionEvaluator(
      new CustomPermissionEvaluator(companyDao, projectDao, taskDao));
    return expressionHandler;
  }

  @Bean
  @Order(1)
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
      .securityMatcher("/api/v1/**")
      .authorizeHttpRequests(authorizeRequestsConfigurer -> authorizeRequestsConfigurer
        .requestMatchers("/api/v1/admin", "/api/v1/admin/**").hasAuthority(GlobalRole.ADMIN.name())
        .requestMatchers("/api/v1/user", "/api/v1/user/**").hasAuthority(GlobalRole.USER.name())
        .anyRequest().permitAll())
      .csrf(AbstractHttpConfigurer::disable)
      .formLogin(AbstractHttpConfigurer::disable)
      .httpBasic(AbstractHttpConfigurer::disable)
      .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
      .build();
  }
}