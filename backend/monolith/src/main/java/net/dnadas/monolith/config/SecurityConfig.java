package net.dnadas.monolith.config;

import lombok.RequiredArgsConstructor;
import net.dnadas.monolith.model.company.CompanyDao;
import net.dnadas.monolith.model.company.project.ProjectDao;
import net.dnadas.monolith.model.company.project.task.TaskDao;
import net.dnadas.monolith.service.authorization.CustomPermissionEvaluator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
  private final CompanyDao companyDao;
  private final ProjectDao projectDao;
  private final TaskDao taskDao;
  private final RestTemplate restTemplate;

  // Method-level security
  @Bean
  public MethodSecurityExpressionHandler expressionHandler() {
    var expressionHandler = new DefaultMethodSecurityExpressionHandler();
    expressionHandler.setPermissionEvaluator(
      new CustomPermissionEvaluator(companyDao, projectDao, taskDao, restTemplate));
    return expressionHandler;
  }
}