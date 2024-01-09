package com.codecool.tasx.controller.dto.company;

import com.codecool.tasx.controller.dto.company.reward.RewardResponseDto;
import com.codecool.tasx.controller.dto.user.UserResponsePublicDto;

import java.util.List;

public record CompanyResponsePrivateDTO(
  Long companyId, String name, String description,
  UserResponsePublicDto companyOwner,
  List<UserResponsePublicDto> employees,
  List<RewardResponseDto> rewards
) {
}
