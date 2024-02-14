package com.codecool.tasx.service.email;

import com.codecool.tasx.controller.dto.email.EmailRequestDto;
import com.codecool.tasx.exception.email.EmailAddressFormatException;
import com.codecool.tasx.exception.email.EmailContentFormatException;
import com.codecool.tasx.exception.email.EmailSubjectFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  private final JavaMailSender javaMailSender;

  @Value("${BACKEND_SMTP_USERNAME}")
  private String systemSmtpAddress;

  @Autowired
  public EmailService(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  public void sendMailToUserAddress(EmailRequestDto mailRequest) throws MailException,
    EmailAddressFormatException, EmailSubjectFormatException, EmailContentFormatException {
    mailRequest.validate();
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(mailRequest.to());
    message.setFrom(systemSmtpAddress);
    message.setSubject(mailRequest.subject());
    message.setText(mailRequest.content());
    javaMailSender.send(message);
  }
}
