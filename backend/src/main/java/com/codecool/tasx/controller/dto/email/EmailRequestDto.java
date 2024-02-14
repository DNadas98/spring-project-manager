package com.codecool.tasx.controller.dto.email;

import com.codecool.tasx.exception.email.EmailAddressFormatException;
import com.codecool.tasx.exception.email.EmailContentFormatException;
import com.codecool.tasx.exception.email.EmailSubjectFormatException;

import java.util.regex.Pattern;

public record EmailRequestDto(String to, String subject, String content) {

  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,100}$");
  private static final int SUBJECT_MIN_LENGTH = 5;
  private static final int SUBJECT_MAX_LENGTH = 100;
  private static final int CONTENT_MIN_LENGTH = 10;
  private static final int CONTENT_MAX_LENGTH = 500;

  public void validate()
    throws EmailAddressFormatException, EmailSubjectFormatException, EmailContentFormatException {
    if (to == null || to.trim().isEmpty() || !EMAIL_PATTERN.matcher(to).matches()) {
      throw new EmailAddressFormatException();
    }
    if (subject == null || subject.trim().length() < SUBJECT_MIN_LENGTH ||
      subject.trim().length() > SUBJECT_MAX_LENGTH) {
      throw new EmailSubjectFormatException();
    }
    if (content == null || content.trim().length() < CONTENT_MIN_LENGTH ||
      content.trim().length() > CONTENT_MAX_LENGTH) {
      throw new EmailContentFormatException();
    }
  }

  @Override
  public String toString() {
    return String.format("E-email to: %s, Subject: %s, Content: %s", to, subject, content);
  }
}