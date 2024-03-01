export function toZonedStringWithOffset(clientDateTimeString: FormDataEntryValue | null): string {
  if (!clientDateTimeString || typeof clientDateTimeString !== 'string' || !clientDateTimeString.length) {
    throw new Error("Invalid date-time received");
  }
  const localDate = new Date(clientDateTimeString);
  const formattedDate = localDate.toISOString();
  return formattedDate;
}

export function plusHours(date: Date, hours: number): Date {
  return new Date(date.getTime() + hours * 60 * 60 * 1000);
}

export function minusHours(date: Date, hours: number): Date {
  return new Date(date.getTime() - hours * 60 * 60 * 1000);
}

export function plusMinutes(date: Date, minutes: number): Date {
  return new Date(date.getTime() + minutes * 60 * 1000);
}

export function minusMinutes(date: Date, minutes: number): Date {
  return new Date(date.getTime() - minutes * 60 * 1000);
}
