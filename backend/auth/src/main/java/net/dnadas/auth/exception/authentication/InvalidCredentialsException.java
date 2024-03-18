package net.dnadas.auth.exception.authentication;

public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException() {
    super("Invalid credentials");
  }
}
