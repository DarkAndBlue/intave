package de.jpx3.intave.diagnostic.message;

/**
 * Class generated using IntelliJ IDEA
 * Created by Richard Strunk 2022
 */

public enum PrefixDetail {
  FULL(new FullFormatter()),
  REDUCED(new ReducedFormatter()),
  REDUCED_NO_PREFIX(new ReducedNoPrefixFormatter()),
  NONE(new NoneFormatter()),

  ;

  private final Formatter formatter;

  PrefixDetail(Formatter formatter) {
    this.formatter = formatter;
  }

  public String formatPrefix(MessageSeverity severity, String name) {
    return formatter.format(severity, name);
  }

  private static class FullFormatter implements Formatter {
    @Override
    public String format(MessageSeverity severity, String name) {
      return String.format("[DM/%s]", name);
    }
  }

  private static class ReducedFormatter implements Formatter {
    @Override
    public String format(MessageSeverity severity, String name) {
      return String.format("[%s]", name);
    }
  }

  private static class ReducedNoPrefixFormatter implements Formatter {
    @Override
    public String format(MessageSeverity severity, String name) {
      return name;
    }
  }

  private static class NoneFormatter implements Formatter {
    @Override
    public String format(MessageSeverity severity, String name) {
      return "";
    }
  }
  public interface Formatter {
    String format(MessageSeverity severity, String name);

  }
}
