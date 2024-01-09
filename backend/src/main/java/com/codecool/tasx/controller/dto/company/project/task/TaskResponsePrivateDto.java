package com.codecool.tasx.controller.dto.company.project.task;

import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;
import com.codecool.tasx.model.company.project.task.Importance;
import com.codecool.tasx.model.company.project.task.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

public record TaskResponsePrivateDto(Long projectId, Long taskId, String name, String description,
                                     Importance importance, Integer difficulty,
                                     LocalDateTime startDate,
                                     LocalDateTime deadline, TaskStatus taskStatus,
                                     UserResponsePublicDto owner,
                                     List<UserResponsePublicDto> assignedEmployees, long points) {
}
