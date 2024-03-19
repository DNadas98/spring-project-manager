package net.dnadas.companies.exception.company.project;

public class DuplicateProjectJoinRequestException extends RuntimeException {
  public DuplicateProjectJoinRequestException() {
    super("Project join request already exists");
  }
}
