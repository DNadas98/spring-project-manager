package net.dnadas.companies.dto.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import net.dnadas.companies.dto.company.project.ProjectResponsePublicDTO;
import net.dnadas.companies.model.request.RequestStatus;

public record ProjectJoinRequestResponseDto(
  @NotNull @Min(1) Long requestId,
  @NotNull ProjectResponsePublicDTO project,
  net.dnadas.auth.dto.user.UserResponsePublicDto user,
  @NotNull RequestStatus status
) {
}
