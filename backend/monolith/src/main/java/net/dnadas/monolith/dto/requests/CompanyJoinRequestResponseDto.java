package net.dnadas.monolith.dto.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import net.dnadas.monolith.dto.company.CompanyResponsePublicDTO;
import net.dnadas.monolith.model.request.RequestStatus;

public record CompanyJoinRequestResponseDto(
  @NotNull @Min(1) Long requestId,
  @NotNull CompanyResponsePublicDTO company,
  net.dnadas.auth.dto.user.UserResponsePublicDto user,
  @NotNull RequestStatus status) {
}
