package net.dnadas.companies.exception.company;

import lombok.Getter;

@Getter
public class CompanyNotFoundException extends RuntimeException {
  private final Long id;

  public CompanyNotFoundException(Long id) {
    super("Company with ID " + id + " was not found");
    this.id = id;
  }

}
