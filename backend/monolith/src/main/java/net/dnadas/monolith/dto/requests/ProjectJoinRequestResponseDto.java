package net.dnadas.monolith.dto.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import net.dnadas.monolith.dto.company.project.ProjectResponsePublicDTO;
import net.dnadas.monolith.auth.dto.user.UserResponsePublicDto;
import net.dnadas.monolith.model.request.RequestStatus;

public record ProjectJoinRequestResponseDto(
  @NotNull @Min(1) Long requestId,
  @NotNull ProjectResponsePublicDTO project,
  @NotNull @Valid UserResponsePublicDto user,
  @NotNull RequestStatus status
) {
}
