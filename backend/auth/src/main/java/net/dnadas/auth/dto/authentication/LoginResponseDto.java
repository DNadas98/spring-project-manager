package net.dnadas.auth.dto.authentication;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record LoginResponseDto(
  @NotNull @Length(min = 1) String accessToken,
  @NotNull @Valid UserInfoDto userInfo) {

}
