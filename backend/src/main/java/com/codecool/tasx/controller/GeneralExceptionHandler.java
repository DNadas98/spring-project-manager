package com.codecool.tasx.controller;

import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.exception.company.DuplicateCompanyJoinRequestException;
import com.codecool.tasx.exception.company.UserAlreadyInCompanyException;
import com.codecool.tasx.exception.user.UserNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GeneralExceptionHandler {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<?> handleCustomUnauthorized(UnauthorizedException e) {
    logger.error(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<?> handleCustomUnauthorized(UsernameNotFoundException e) {
    logger.error(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<?> handleCustomUnauthorized(UserNotFoundException e) {
    logger.error(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Unauthorized"));
  }

  @ExceptionHandler(CompanyNotFoundException.class)
  public ResponseEntity<?> handleCompanyNotFound(CompanyNotFoundException e) {
    logger.error(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
      Map.of("error", "The requested company was not found"));
  }

  @ExceptionHandler(UserAlreadyInCompanyException.class)
  public ResponseEntity<?> handleUserAlreadyInCompany(UserAlreadyInCompanyException e) {
    logger.error(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
      Map.of("error", "User is already in the requested company"));
  }

  @ExceptionHandler(DuplicateCompanyJoinRequestException.class)
  public ResponseEntity<?> handleDuplicateCompanyJoinRequest(
    DuplicateCompanyJoinRequestException e) {
    logger.error(e.getMessage(), e);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(
      Map.of("error", "Join request already exists with the provided details"));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<?> handleDuplicateFields(ConstraintViolationException e) {
    logger.error(e.getMessage(), e);
    if (e.getMessage().contains("unique constraint")) {
      String errorMessage = getConstraintErrorMessage(e.getMessage());
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", errorMessage));
    }
    throw e;
  }

  /**
   * Use this to customize error messages for any constraint violation<br/>
   * Blame ChatGPT for any regex🤮 related issues<br/>
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
}
