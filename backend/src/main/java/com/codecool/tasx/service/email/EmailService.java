package com.codecool.tasx.service.email;

import com.codecool.tasx.controller.dto.email.EmailRequestDto;
import com.codecool.tasx.exception.email.EmailAddressFormatException;
import com.codecool.tasx.exception.email.EmailContentFormatException;
import com.codecool.tasx.exception.email.EmailSubjectFormatException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
    EmailAddressFormatException, EmailSubjectFormatException, EmailContentFormatException,
    MessagingException {
    mailRequest.validate();
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
    helper.setTo(mailRequest.to());
    helper.setFrom(systemSmtpAddress);
    helper.setSubject(mailRequest.subject());
    helper.setText(mailRequest.content(), true);
    javaMailSender.send(message);
  }
}
