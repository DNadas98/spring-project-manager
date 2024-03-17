package net.dnadas.monolith.auth.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import net.dnadas.monolith.auth.model.account.AccountType;

public record TokenPayloadDto(@NotNull @Email String email, @NotNull AccountType accountType) {
}
