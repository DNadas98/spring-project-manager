package net.dnadas.auth.exception.account;

public class OnlyOneAccountFoundException extends RuntimeException {
  public OnlyOneAccountFoundException() {
    super("No other user accounts were found");
  }
}
