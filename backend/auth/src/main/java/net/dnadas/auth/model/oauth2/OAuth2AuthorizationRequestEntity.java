package net.dnadas.auth.model.oauth2;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.*;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class OAuth2AuthorizationRequestEntity implements Serializable {
  @Id
  private String id;

  @Lob
  private String serialization;

  @Override
  public String toString() {
    return "OAuth2AuthorizationRequestEntity{" +
      "id='" + id + '\'' +
      '}';
  }
}
