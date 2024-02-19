package com.codecool.tasx.dto.company.project;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record ProjectCreateRequestDto(
  @NotNull @Length(min = 1, max = 50) String name,
  @NotNull @Length(min = 1, max = 500) String description,
  @NotNull @FutureOrPresent LocalDateTime startDate,
  @NotNull @FutureOrPresent LocalDateTime deadline) {
}
