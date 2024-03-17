package net.dnadas.monolith.exception.verification;

public class VerificationTokenAlreadyExistsException extends RuntimeException {
  public VerificationTokenAlreadyExistsException() {
    super("Verification process with the provided details is already started");
  }
}
