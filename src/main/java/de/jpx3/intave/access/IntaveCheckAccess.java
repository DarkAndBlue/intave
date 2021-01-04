package de.jpx3.intave.access;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public interface IntaveCheckAccess {
  String name();
  String configurationKey();
  String category();
  String description();
  boolean enabled();
  int violationLevelOf(Player player);
  void addViolationPoints(Player player, int amount);
  void resetViolationLevel(Player player);
  void setDecayEnabled(boolean decayEnabled);
  Map<Integer, List<String>> globalThresholds();
  List<String> thresholdsOf(int violationLevel);
  Map<Integer, List<String>> thresholdsBetween(int from, int to);
  IntaveCheckStatisticsAccess statistics();
}