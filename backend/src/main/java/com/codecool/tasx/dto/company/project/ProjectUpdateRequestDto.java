package com.codecool.tasx.dto.company.project;

import java.time.LocalDateTime;

public record ProjectUpdateRequestDto(String name,
                                      String description,
                                      LocalDateTime startDate, LocalDateTime deadline) {
}
