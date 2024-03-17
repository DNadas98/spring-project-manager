package net.dnadas.monolith.auth.exception.account;

public class AccountAlreadyExistsException extends RuntimeException {
  public AccountAlreadyExistsException() {
    super("User account already exists");
  }

  public AccountAlreadyExistsException(String message) {
    super(message);
  }
}
