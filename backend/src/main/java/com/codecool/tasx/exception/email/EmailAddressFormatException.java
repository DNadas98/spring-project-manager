package com.codecool.tasx.exception.email;

public class EmailAddressFormatException extends RuntimeException {
  public EmailAddressFormatException() {
    super("Invalid e-email address format");
  }
}
