package net.dnadas.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record VerificationEmailRequestDto(
  @NotNull @Min(1) Long verificationId,
  @NotNull @Length(min = 1,
    max = 100) String verificationCode,
  @NotNull @Length(min = 1, max = 100) String username,
  @NotNull @Email String toEmail) {
}
