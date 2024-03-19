package net.dnadas.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouteConfig {
  private final RestTemplate restTemplate;

  @Bean
  public RouterFunction<ServerResponse> authRoute() {
    return route(
      RequestPredicates.path("/api/v1/auth/**")
        .or(RequestPredicates.path("/api/v1/user"))
        .or(RequestPredicates.path("/api/v1/user/**"))
        .or(RequestPredicates.path("/oauth2"))
        .or(RequestPredicates.path("/oauth2/**")),
      HandlerFunctions.http()).filter(lb("AUTH"));
  }

  @Bean
  public RouterFunction<ServerResponse> companyRoute() {
    return route(
      RequestPredicates.path("/api/v1/companies")
        .or(RequestPredicates.path("/api/v1/companies/**")),
      HandlerFunctions.http()).filter(lb("COMPANIES"));
  }
}
