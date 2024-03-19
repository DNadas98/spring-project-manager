package net.dnadas.companies.exception.company.project;

public class UserAlreadyInProjectException extends RuntimeException {
  public UserAlreadyInProjectException() {
    super("User is already in the project");
  }
}
