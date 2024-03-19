package net.dnadas.email.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dnadas.email.dto.EmailRequestDto;
import net.dnadas.email.dto.VerificationEmailRequestDto;
import net.dnadas.email.service.EmailService;
import net.dnadas.email.service.EmailTemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
@Slf4j
public class EMailController {
  private final EmailService emailService;
  private final EmailTemplateService emailTemplateService;

  @PostMapping("/send/registration-verification")
  public ResponseEntity<?> sendRegistrationVerificationEmail(
    @RequestBody VerificationEmailRequestDto dto) throws MessagingException {
    try {
      String registrationEmailContent = emailTemplateService.getRegistrationEmailContent(
        dto.verificationCode(), dto.verificationId(), dto.username());
      emailService.sendMailToUserAddress(new EmailRequestDto(
        dto.toEmail(), "Registration verification to Spring Project Manager",
        registrationEmailContent));
      return ResponseEntity.ok().build();
    } catch (IOException e) {
      log.error("Error occurred while sending registration verification email", e);
      return ResponseEntity.internalServerError().build();
    }
  }
}
