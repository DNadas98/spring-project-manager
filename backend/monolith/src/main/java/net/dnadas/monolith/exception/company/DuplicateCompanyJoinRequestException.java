package net.dnadas.monolith.exception.company;

public class DuplicateCompanyJoinRequestException extends RuntimeException {
  public DuplicateCompanyJoinRequestException() {
    super("Company join request already exists");
  }
}
