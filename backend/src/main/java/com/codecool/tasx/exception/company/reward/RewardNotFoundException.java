package com.codecool.tasx.exception.company.reward;

public class RewardNotFoundException extends RuntimeException {

  private final Long id;

  public RewardNotFoundException(Long id) {
    super();
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
