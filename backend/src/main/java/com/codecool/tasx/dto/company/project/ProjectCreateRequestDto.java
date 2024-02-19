package com.codecool.tasx.dto.company.project;

import java.time.LocalDateTime;

public record ProjectCreateRequestDto(String name, String description,
                                      LocalDateTime startDate, LocalDateTime deadline) {
}
