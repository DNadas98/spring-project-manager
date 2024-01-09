package com.codecool.tasx.service.converter;

import com.codecool.tasx.controller.dto.company.reward.RewardResponseDto;
import com.codecool.tasx.model.company.reward.Reward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RewardConverter {
  private final TaskConverter taskConverter;

  @Autowired
  public RewardConverter(TaskConverter taskConverter) {
    this.taskConverter = taskConverter;
  }

  public RewardResponseDto getRewardResponseDto(Reward reward) {
    return new RewardResponseDto(reward.getCompany().getId(),
      reward.getId(), reward.getName(), reward.getDescription(),
      taskConverter.getTaskResponsePublicDtos(reward.getTasks()), reward.getPointCost());
  }

  public List<RewardResponseDto> getRewardResponseDtos(List<Reward> rewards) {
    return rewards.stream().map(reward -> getRewardResponseDto(reward)).collect(
      Collectors.toList());
  }
}
