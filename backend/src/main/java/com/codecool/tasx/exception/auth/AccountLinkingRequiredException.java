package com.codecool.tasx.exception.auth;

public class AccountLinkingRequiredException extends RuntimeException {
  public AccountLinkingRequiredException() {
  }

  public AccountLinkingRequiredException(String message) {
    super(message);
  }
}
