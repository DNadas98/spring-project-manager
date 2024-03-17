package net.dnadas.monolith.exception.auth;

public class OnlyOneAccountFoundException extends RuntimeException {
  public OnlyOneAccountFoundException() {
    super("No other user accounts were found");
  }
}
