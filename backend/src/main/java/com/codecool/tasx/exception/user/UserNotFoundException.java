package com.codecool.tasx.exception.user;

public class UserNotFoundException extends RuntimeException {
  private final Long id;

  public UserNotFoundException(Long id) {
    super("User with ID " + id + " was not found");
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
