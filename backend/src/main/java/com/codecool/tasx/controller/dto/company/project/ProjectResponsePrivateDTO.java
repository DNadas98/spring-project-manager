package com.codecool.tasx.controller.dto.company.project;

import com.codecool.tasx.controller.dto.company.project.task.TaskResponsePublicDto;
import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectResponsePrivateDTO(Long companyId, Long projectId, String name,
                                        String description, LocalDateTime startDate,
                                        LocalDateTime deadline,
                                        UserResponsePublicDto projectOwner,
                                        List<UserResponsePublicDto> assignedEmployees,
                                        List<TaskResponsePublicDto> tasks) {
}
