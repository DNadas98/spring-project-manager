package net.dnadas.monolith.exception.datetime;

public class InvalidDateTimeReceivedException extends DateTimeBadRequestException {
  public InvalidDateTimeReceivedException() {
    super("The received date-time format is invalid");
  }
}
