package com.codecool.tasx.exception.company.project;

public class ProjectNotFoundException extends RuntimeException {
  private final Long id;

  public ProjectNotFoundException(Long id) {
    super();
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
