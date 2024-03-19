package net.dnadas.companies.exception.datetime;

public abstract class DateTimeBadRequestException extends RuntimeException {
  protected DateTimeBadRequestException(String message) {
    super(message);
  }
}
