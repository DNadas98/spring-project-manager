package com.codecool.tasx.exception.company;

public class CompanyJoinRequestNotFoundException extends RuntimeException {
  private final Long id;

  public CompanyJoinRequestNotFoundException(Long id) {
    super();
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
