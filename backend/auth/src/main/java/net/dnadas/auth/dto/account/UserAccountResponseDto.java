package net.dnadas.auth.dto.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import net.dnadas.auth.model.account.AccountType;

public record UserAccountResponseDto(
  @NotNull @Min(1) Long id, @NotNull @Email String email,
  @NotNull AccountType accountType) {
}
