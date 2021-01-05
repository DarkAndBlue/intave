package de.jpx3.intave.access;

public interface PlayerAccess {
  int protocolVersion();
  String minecraftVersion();

  int violationLevel(String check);
  void addViolationPoints(String check, int amount);
  void resetViolationLevel(String check);

  boolean syncMarginOverflow();

  TrustFactor trustFactor();

  PlayerNetStatistics networkStatistics();
  PlayerClickStatistics clickStatistics();

  void clearCombatData();
}
