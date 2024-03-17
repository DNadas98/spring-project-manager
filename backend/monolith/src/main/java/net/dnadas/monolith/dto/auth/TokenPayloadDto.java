package net.dnadas.monolith.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import net.dnadas.monolith.model.auth.account.AccountType;

public record TokenPayloadDto(@NotNull @Email String email, @NotNull AccountType accountType) {
}
