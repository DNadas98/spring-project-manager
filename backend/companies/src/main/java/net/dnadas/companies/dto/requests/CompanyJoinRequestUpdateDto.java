package net.dnadas.companies.dto.requests;

import jakarta.validation.constraints.NotNull;
import net.dnadas.companies.model.request.RequestStatus;

public record CompanyJoinRequestUpdateDto(@NotNull RequestStatus status) {
}
