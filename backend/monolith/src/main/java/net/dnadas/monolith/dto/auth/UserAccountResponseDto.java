package net.dnadas.monolith.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import net.dnadas.monolith.model.auth.account.AccountType;

public record UserAccountResponseDto(
  @NotNull @Min(1) Long id, @NotNull @Email String email,
  @NotNull AccountType accountType) {
}
