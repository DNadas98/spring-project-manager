package com.codecool.tasx.controller.dto.requests;

import com.codecool.tasx.controller.dto.company.CompanyResponsePublicDTO;
import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;
import com.codecool.tasx.model.requests.RequestStatus;

public record CompanyJoinRequestResponseDto(
  Long requestId, CompanyResponsePublicDTO company, UserResponsePublicDto user,
  RequestStatus status) {
}
