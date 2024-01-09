package com.codecool.tasx.controller.dto.company.reward;

import java.util.List;

public record RewardCreateRequestDto(Long id, Long userId, List<Long> taskIds,
                                     String name, String description, Long pointCost) {
}
