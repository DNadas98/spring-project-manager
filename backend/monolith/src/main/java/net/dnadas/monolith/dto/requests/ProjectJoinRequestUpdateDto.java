package net.dnadas.monolith.dto.requests;

import jakarta.validation.constraints.NotNull;
import net.dnadas.monolith.model.request.RequestStatus;

public record ProjectJoinRequestUpdateDto(
  @NotNull RequestStatus status
) {
}
