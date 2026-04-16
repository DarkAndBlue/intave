package de.jpx3.intave.diagnostic.message;

public enum MessageSeverity {
  LOW(1),
  MEDIUM(2),
  HIGH(3),

  ;

  private final int severity;

  MessageSeverity(int severity) {
    this.severity = severity;
  }

  public int severity() {
    return severity;
  }

  public boolean isLowerThan(MessageSeverity other) {
    return severity < other.severity;
  }

  public boolean isHigherThan(MessageSeverity other) {
    return severity > other.severity;
  }
}
