package de.jpx3.intave.access;

public final class IntaveAccessViolationException extends IntaveException {
  public IntaveAccessViolationException() {
    super();
  }

  public IntaveAccessViolationException(String message) {
    super(message);
  }

  public IntaveAccessViolationException(String message, Throwable cause) {
    super(message, cause);
  }

  public IntaveAccessViolationException(Throwable cause) {
    super(cause);
  }
}
