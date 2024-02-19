package com.codecool.tasx.dto.requests;

import com.codecool.tasx.dto.company.CompanyResponsePublicDTO;
import com.codecool.tasx.dto.user.UserResponsePublicDto;
import com.codecool.tasx.model.request.RequestStatus;

public record CompanyJoinRequestResponseDto(
  Long requestId, CompanyResponsePublicDTO company, UserResponsePublicDto user,
  RequestStatus status) {
}
