package de.jpx3.intave.diagnostic.message;

import java.util.function.BinaryOperator;

/**
 * Class generated using IntelliJ IDEA
 * Created by Richard Strunk 2022
 */

public enum MessageDetail {
  UNSPECIFIED(null),
  FULL((full, reduced) -> full),
  REDUCED((full, reduced) -> reduced),

  ;

  private final BinaryOperator<String> selector;

  MessageDetail(BinaryOperator<String> selector) {
    this.selector = selector;
  }

  public String select(String full, String reduced) {
    return selector.apply(full, reduced);
  }
}
