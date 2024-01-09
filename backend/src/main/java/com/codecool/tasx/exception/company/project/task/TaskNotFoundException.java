package com.codecool.tasx.exception.company.project.task;

public class TaskNotFoundException extends RuntimeException {
  private final Long id;

  public TaskNotFoundException(Long id) {
    super();
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
