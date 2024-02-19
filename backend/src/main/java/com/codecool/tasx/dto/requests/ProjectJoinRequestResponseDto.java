package com.codecool.tasx.dto.requests;

import com.codecool.tasx.dto.company.project.ProjectResponsePublicDTO;
import com.codecool.tasx.dto.user.UserResponsePublicDto;
import com.codecool.tasx.model.request.RequestStatus;

public record ProjectJoinRequestResponseDto(
  Long requestId, ProjectResponsePublicDTO project, UserResponsePublicDto user,
  RequestStatus status
) {
}
