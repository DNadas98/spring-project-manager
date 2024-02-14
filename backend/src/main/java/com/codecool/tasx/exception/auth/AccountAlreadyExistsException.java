package com.codecool.tasx.exception.auth;

public class AccountAlreadyExistsException extends RuntimeException {
  public AccountAlreadyExistsException() {
  }

  public AccountAlreadyExistsException(String message) {
    super(message);
  }
}
