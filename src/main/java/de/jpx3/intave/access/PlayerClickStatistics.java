package de.jpx3.intave.access;

import java.util.function.Consumer;

public interface PlayerClickStatistics {
  int clicksLastSecond();
  void subscribeToSecond(Consumer<Integer> clicks);
}