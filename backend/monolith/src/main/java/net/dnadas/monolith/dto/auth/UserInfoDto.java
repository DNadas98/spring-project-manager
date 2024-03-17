package net.dnadas.monolith.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import net.dnadas.monolith.model.auth.account.AccountType;
import net.dnadas.monolith.model.user.GlobalRole;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

public record UserInfoDto(
  @NotNull @Length(min = 1, max = 50) String username,
  @NotNull @Email String email,
  @NotNull AccountType accountType,
  @NotNull Set<GlobalRole> roles) {
}
