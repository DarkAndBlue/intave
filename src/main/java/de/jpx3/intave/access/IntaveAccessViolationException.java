package de.jpx3.intave.access;

import de.jpx3.intave.access.linked.exceptions.IntaveException;

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
