package net.dnadas.companies.exception.company;

public class UserAlreadyInCompanyException extends RuntimeException {
  public UserAlreadyInCompanyException() {
    super("User is already employee of the company");
  }
}
