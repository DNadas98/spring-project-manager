package com.codecool.tasx.dto.company.project;

import com.codecool.tasx.dto.company.project.task.TaskResponsePublicDto;

import java.time.LocalDateTime;
import java.util.List;

public record ProjectResponsePrivateDTO(Long companyId, Long projectId, String name,
                                        String description, LocalDateTime startDate,
                                        LocalDateTime deadline,
                                        List<TaskResponsePublicDto> tasks) {
}
