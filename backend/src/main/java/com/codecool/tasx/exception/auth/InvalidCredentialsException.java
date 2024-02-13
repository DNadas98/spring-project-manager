package com.codecool.tasx.exception.auth;

public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException() {
  }

  public InvalidCredentialsException(String message) {
    super(message);
  }
}
