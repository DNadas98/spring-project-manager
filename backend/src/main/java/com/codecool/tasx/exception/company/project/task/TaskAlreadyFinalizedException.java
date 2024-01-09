package com.codecool.tasx.exception.company.project.task;

public class TaskAlreadyFinalizedException extends RuntimeException {
  private final Long id;

  public TaskAlreadyFinalizedException(Long id) {
    super();
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
