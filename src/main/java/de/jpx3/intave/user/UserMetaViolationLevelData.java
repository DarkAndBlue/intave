package de.jpx3.intave.user;

import com.google.common.collect.Maps;
import de.jpx3.intave.access.player.trust.TrustFactor;

import java.util.Map;

public final class UserMetaViolationLevelData {
  public double physicsVL;
  public double physicsInvalidMovementsInRow;
  public volatile boolean isInActiveTeleportBundle;

  public Map<String, Map<String, Double>> violationLevel = Maps.newConcurrentMap();
  public Map<String, Map<String, Double>> violationLevelGainedCounter = Maps.newConcurrentMap();
  public Map<String, Map<String, Long>> lastViolationLevelGainedCounterReset = Maps.newConcurrentMap();
  public TrustFactor trustFactor = TrustFactor.YELLOW;
}