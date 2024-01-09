package com.codecool.tasx.exception.auth;

public class OAuth2ProcessingException extends RuntimeException {
  public OAuth2ProcessingException() {
  }

  public OAuth2ProcessingException(String message) {
    super(message);
  }
}
