package net.dnadas.companies.dto.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import net.dnadas.companies.dto.company.CompanyResponsePublicDTO;
import net.dnadas.companies.model.request.RequestStatus;

public record CompanyJoinRequestResponseDto(
  @NotNull @Min(1) Long requestId,
  @NotNull CompanyResponsePublicDTO company,
  net.dnadas.auth.dto.user.UserResponsePublicDto user,
  @NotNull RequestStatus status) {
}
