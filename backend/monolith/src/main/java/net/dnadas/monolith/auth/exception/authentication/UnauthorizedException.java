package net.dnadas.monolith.auth.exception.authentication;

public class UnauthorizedException extends RuntimeException {
  public UnauthorizedException() {
    super("Unauthorized");
  }

  public UnauthorizedException(String message) {
    super(message);
  }
}
