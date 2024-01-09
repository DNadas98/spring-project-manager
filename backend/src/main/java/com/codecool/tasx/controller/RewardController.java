package com.codecool.tasx.controller;

import com.codecool.tasx.controller.dto.company.reward.RewardCreateRequestDto;
import com.codecool.tasx.controller.dto.company.reward.RewardResponseDto;
import com.codecool.tasx.controller.dto.company.reward.RewardUpdateRequestDto;
import com.codecool.tasx.service.company.reward.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/companies/{companyId}/rewards")
public class RewardController {
  private final RewardService rewardService;

  @Autowired
  public RewardController(RewardService rewardService) {
    this.rewardService = rewardService;
  }

  @GetMapping
  public ResponseEntity<?> getAllRewards(@PathVariable Long companyId) {
    List<RewardResponseDto> rewards = rewardService.getAllRewards(companyId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", rewards));
  }

  @GetMapping("/{rewardId}")
  public ResponseEntity<?> getRewardById(
    @PathVariable Long companyId, @PathVariable Long rewardId) {
    RewardResponseDto reward = rewardService.getRewardById(companyId, rewardId);
    return ResponseEntity.status(HttpStatus.OK).body(Map.of("data", reward));
  }

  @PostMapping
  public ResponseEntity<?> createReward(
    @PathVariable Long companyId, @RequestBody RewardCreateRequestDto rewardDetails) {
    RewardResponseDto rewardResponseDetails = rewardService.createReward(rewardDetails, companyId);
    return ResponseEntity.status(HttpStatus.CREATED).body(
      Map.of("message", "Reward created successfully", "data", rewardResponseDetails));
  }

  @PutMapping("/{rewardId}")
  public ResponseEntity<?> updateReward(
    @PathVariable Long companyId, @PathVariable Long rewardId,
    @RequestBody RewardUpdateRequestDto rewardDetails) {
    RewardResponseDto rewardResponseDetails = rewardService.updateReward(rewardDetails, companyId,
      rewardId);
    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Reward with ID " + rewardId + " updated successfully", "data",
        rewardResponseDetails));
  }

  @DeleteMapping("/{rewardId}")
  public ResponseEntity<?> deleteReward(@PathVariable Long companyId, @PathVariable Long rewardId) {
    rewardService.deleteReward(rewardId, companyId);
    return ResponseEntity.status(HttpStatus.OK).body(
      Map.of("message", "Reward with ID " + rewardId + " deleted successfully"));
  }

}
