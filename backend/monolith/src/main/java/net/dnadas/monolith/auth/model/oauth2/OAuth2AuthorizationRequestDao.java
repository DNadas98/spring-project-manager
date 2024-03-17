package net.dnadas.monolith.auth.model.oauth2;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuth2AuthorizationRequestDao
  extends JpaRepository<OAuth2AuthorizationRequestEntity, String> {
}
