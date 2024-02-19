package com.codecool.tasx.exception.auth;

public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException() {
    super("Invalid credentials");
  }

  public InvalidCredentialsException(String message) {
    super(message);
  }
}
