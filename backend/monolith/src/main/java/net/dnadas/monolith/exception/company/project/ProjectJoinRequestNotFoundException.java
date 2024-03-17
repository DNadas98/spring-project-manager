package net.dnadas.monolith.exception.company.project;

import lombok.Getter;

@Getter
public class ProjectJoinRequestNotFoundException extends RuntimeException {
  private final Long id;

  public ProjectJoinRequestNotFoundException(Long id) {
    super("Project join request was not found");
    this.id = id;
  }

}
