package net.dnadas.auth.exception.oauth2;

public class OAuth2ProcessingException extends RuntimeException {
  public OAuth2ProcessingException() {
    super("An error occurred while processing the account request");
  }

  public OAuth2ProcessingException(String message) {
    super(message);
  }
}
