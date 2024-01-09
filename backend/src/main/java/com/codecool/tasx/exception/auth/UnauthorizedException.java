package com.codecool.tasx.exception.auth;

public class UnauthorizedException extends RuntimeException {
  public UnauthorizedException() {
  }

  public UnauthorizedException(String message) {
    super(message);
  }
}
