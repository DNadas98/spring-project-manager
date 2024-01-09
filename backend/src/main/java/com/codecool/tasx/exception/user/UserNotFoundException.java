package com.codecool.tasx.exception.user;

public class UserNotFoundException extends RuntimeException {
  private final Long id;

  public UserNotFoundException(Long id) {
    super();
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
