package net.dnadas.monolith.auth.dto.authentication;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record RefreshRequestDto(
  @NotNull @Length(min = 1) String refreshToken
) {
}
