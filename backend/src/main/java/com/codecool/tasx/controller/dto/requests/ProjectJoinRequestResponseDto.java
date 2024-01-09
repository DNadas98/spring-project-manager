package com.codecool.tasx.controller.dto.requests;

import com.codecool.tasx.controller.dto.company.project.ProjectResponsePublicDTO;
import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;
import com.codecool.tasx.model.requests.RequestStatus;

public record ProjectJoinRequestResponseDto(
  Long requestId, ProjectResponsePublicDTO project, UserResponsePublicDto user,
  RequestStatus status
) {
}
