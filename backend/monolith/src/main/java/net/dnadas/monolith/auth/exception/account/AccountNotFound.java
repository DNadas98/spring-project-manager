package net.dnadas.monolith.auth.exception.account;

public class AccountNotFound extends RuntimeException {
  public AccountNotFound(Long id) {
    super("User account with ID " + id + " was not found");
  }
}
