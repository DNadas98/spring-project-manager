package com.codecool.tasx.dto.requests;

import com.codecool.tasx.dto.company.CompanyResponsePublicDTO;
import com.codecool.tasx.dto.user.UserResponsePublicDto;
import com.codecool.tasx.model.request.RequestStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CompanyJoinRequestResponseDto(
  @NotNull @Min(1) Long requestId,
  @NotNull CompanyResponsePublicDTO company,
  @NotNull @Valid UserResponsePublicDto user,
  @NotNull RequestStatus status) {
}
