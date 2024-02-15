package com.codecool.tasx.exception.email;

public class EmailContentFormatException extends RuntimeException {
  public EmailContentFormatException() {
    super("Invalid e-email content format");
  }
}
