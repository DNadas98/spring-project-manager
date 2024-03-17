package net.dnadas.monolith.auth.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import net.dnadas.monolith.auth.model.account.AccountType;
import net.dnadas.monolith.auth.model.user.GlobalRole;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

public record UserInfoDto(
  @NotNull @Length(min = 1, max = 50) String username,
  @NotNull @Email String email,
  @NotNull AccountType accountType,
  @NotNull Set<GlobalRole> roles) {
}
