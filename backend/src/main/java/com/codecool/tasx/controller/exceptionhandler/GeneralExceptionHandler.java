package com.codecool.tasx.controller.exceptionhandler;

import com.codecool.tasx.exception.auth.AccountAlreadyExistsException;
import com.codecool.tasx.exception.auth.UnauthorizedException;
import com.codecool.tasx.exception.company.CompanyJoinRequestNotFoundException;
import com.codecool.tasx.exception.company.CompanyNotFoundException;
import com.codecool.tasx.exception.company.DuplicateCompanyJoinRequestException;
import com.codecool.tasx.exception.company.UserAlreadyInCompanyException;
import com.codecool.tasx.exception.company.project.ProjectNotFoundException;
import com.codecool.tasx.exception.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GeneralExceptionHandler {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  //400

  @ExceptionHandler(UserAlreadyInCompanyException.class)
  public ResponseEntity<?> handleUserAlreadyInCompany(UserAlreadyInCompanyException e) {
    logger.error(e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
      Map.of("error", "User is already in the requested company"));
  }

  //401

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

  //404

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

  // 409

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
}
