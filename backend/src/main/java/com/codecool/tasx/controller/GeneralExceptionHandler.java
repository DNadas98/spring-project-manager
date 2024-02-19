package com.codecool.tasx.controller;

import com.codecool.tasx.exception.auth.AccountAlreadyExistsException;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.company.CompanyJoinRequestNotFoundException;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.exception.company.DuplicateCompanyJoinRequestException;
import com.codecool.tasx.exception.company.UserAlreadyInCompanyException;
import com.codecool.tasx.exception.company.project.ProjectNotFoundException;
import com.codecool.tasx.exception.user.UserNotFoundException;
import jakarta.mail.Address;
import jakarta.mail.SendFailedException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GeneralExceptionHandler {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  private static Map<String, Object> getErrorDetails(
    List<String> invalidAddressesList, List<String> validUnsentAddressesList,
    List<String> validSentAddressesList) {
    Map<String, Object> errorDetails = new HashMap<>();
    errorDetails.put("message", "An error occurred during the e-mail sending process");
    if (!invalidAddressesList.isEmpty()) {
      errorDetails.put("invalidAddresses", invalidAddressesList);
    }
    if (!validUnsentAddressesList.isEmpty()) {
      errorDetails.put("validUnsentAddresses", validUnsentAddressesList);
    }
    if (!validSentAddressesList.isEmpty()) {
      errorDetails.put("validSentAddresses", validSentAddressesList);
    }
    return errorDetails;
  }

  private static void collectAddresses(
    List<String> invalidAddressesList, Address[] invalidAddresses) {
    invalidAddressesList.addAll(Arrays.stream(invalidAddresses).map(Address::toString)
      .collect(Collectors.toList()));
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<?> handleCustomUnauthorized(UnauthorizedException e) {
    logger.error(e.getMessage() == null ? "Unauthorized" : e.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<?> handleCustomUnauthorized(UsernameNotFoundException e) {
    logger.error(e.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<?> handleCustomUnauthorized(UserNotFoundException e) {
    logger.error(e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
      Map.of("error", "User account was not found"));
  }

  @ExceptionHandler(CompanyNotFoundException.class)
  public ResponseEntity<?> handleCompanyNotFound(CompanyNotFoundException e) {
    logger.error(e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
      Map.of("error", "The requested company was not found"));
  }

  @ExceptionHandler(CompanyJoinRequestNotFoundException.class)
  public ResponseEntity<?> handleCompanyJoinRequestNotFound(CompanyJoinRequestNotFoundException e) {
    logger.error(e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
      Map.of("error", "Company join request with the provided details was not found"));
  }

  @ExceptionHandler(ProjectNotFoundException.class)
  public ResponseEntity<?> handleProjectNotFound(ProjectNotFoundException e) {
    logger.error(e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
      Map.of("error", "The requested project was not found"));
  }

  @ExceptionHandler(UserAlreadyInCompanyException.class)
  public ResponseEntity<?> handleUserAlreadyInCompany(UserAlreadyInCompanyException e) {
    logger.error(e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
      Map.of("error", "User is already in the requested company"));
  }

  @ExceptionHandler(AccountAlreadyExistsException.class)
  public ResponseEntity<?> handleAlreadyExistingAccount(AccountAlreadyExistsException e) {
    logger.error(e.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(
      Map.of("error", "User account with the provided e-mail address already exists"));
  }

  @ExceptionHandler(DuplicateCompanyJoinRequestException.class)
  public ResponseEntity<?> handleDuplicateCompanyJoinRequest(
    DuplicateCompanyJoinRequestException e) {
    logger.error(e.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(
      Map.of("error", "Join request already exists with the provided details"));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<?> handleDuplicateFields(ConstraintViolationException e) {
    logger.error(e.getMessage());
    if (e.getMessage().contains("unique constraint")) {
      String errorMessage = getConstraintErrorMessage(e.getMessage());
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", errorMessage));
    }
    throw e;
  }

  /**
   * Use this to customize error messages for any constraint violation<br/>
   *
   * @return A custom error message based on the related data field
   * @TODO: Refactor this, appending cases to a switch does not seem very OOP
   */
  private String getConstraintErrorMessage(String errorMessage) {
    Pattern pattern = Pattern.compile("Detail: Key \\((.*?)\\)=\\((.*?)\\)");

    Matcher matcher = pattern.matcher(errorMessage);
    if (matcher.find()) {
      String keyName = matcher.group(1);
      String keyValue = matcher.group(2);
      switch (keyName) {
        case "email" -> {
          return "The provided e-mail address is already registered";
        }
        case "username" -> {
          return "The provided username is already taken";
        }
        default -> {
          return "The requested " + keyName + ": " + keyValue + " already exists";
        }
      }
    }
    return "The requested update is conflicting with already existing data";
  }

  @ExceptionHandler({MailSendException.class})
  public ResponseEntity<?> handleMailSendException(MailSendException e) {
    Exception[] exceptions = e.getMessageExceptions();
    List<String> invalidAddressesList = new ArrayList<>();
    List<String> validUnsentAddressesList = new ArrayList<>();
    List<String> validSentAddressesList = new ArrayList<>();

    for (Exception ex : exceptions) {
      if (ex instanceof SendFailedException) {
        handleSendFailureException((SendFailedException) ex, invalidAddressesList,
          validUnsentAddressesList, validSentAddressesList);
      }
    }

    Map<String, Object> errorDetails = getErrorDetails(
      invalidAddressesList, validUnsentAddressesList, validSentAddressesList);

    logger.error("E-mail sending error details: {}", errorDetails);
    if (!invalidAddressesList.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
        "error", "Invalid e-mail address(es) received: " + invalidAddressesList.stream().collect(
          Collectors.joining(", "))
      ));
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
      Map.of("error", errorDetails));
  }

  private void handleSendFailureException(
    SendFailedException ex, List<String> invalidAddressesList,
    List<String> validUnsentAddressesList, List<String> validSentAddressesList) {
    Address[] invalidAddresses = ex.getInvalidAddresses();
    Address[] validUnsentAddresses = ex.getValidUnsentAddresses();
    Address[] validSentAddresses = ex.getValidSentAddresses();

    if (invalidAddresses != null && invalidAddresses.length > 0) {
      collectAddresses(invalidAddressesList, invalidAddresses);
    }
    if (validUnsentAddresses != null && validUnsentAddresses.length > 0) {
      collectAddresses(validUnsentAddressesList, validUnsentAddresses);
    }
    if (validSentAddresses != null && validSentAddresses.length > 0) {
      collectAddresses(validSentAddressesList, validSentAddresses);
    }
  }
}
