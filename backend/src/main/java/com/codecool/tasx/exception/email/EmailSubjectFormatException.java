package com.codecool.tasx.exception.email;

public class EmailSubjectFormatException extends RuntimeException{
  public EmailSubjectFormatException() {
    super("Invalid e-email subject format");
  }
}
