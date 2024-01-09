package com.codecool.tasx.controller.dto.company.project.task;

import com.codecool.tasx.model.company.project.task.Importance;
import com.codecool.tasx.model.company.project.task.TaskStatus;

import java.time.LocalDateTime;

public record TaskCreateRequestDto(Long projectId, String name, String description,
                                   Importance importance, Integer difficulty,
                                   LocalDateTime startDate,
                                   LocalDateTime deadline, TaskStatus taskStatus, long points) {
}
