package de.jpx3.intave.check.combat.heuristics.detect.clickpatterns;

import de.jpx3.intave.check.combat.Heuristics;
import de.jpx3.intave.user.User;
import org.bukkit.Bukkit;

import java.util.List;

import static de.jpx3.intave.check.combat.heuristics.detect.clickpatterns.AirClickLimitHeuristics.AirClickLimitBlueprintMeta;

public final class AirClickLimitHeuristics extends SwingBlueprint<AirClickLimitBlueprintMeta> {
  public AirClickLimitHeuristics(Heuristics parentCheck) {
    super(parentCheck, AirClickLimitBlueprintMeta.class, 60, false, false);
  }

  @Override
  public void check(User user, List<Integer> delays) {
    AirClickLimitBlueprintMeta exampleMeta = metaOf(userOf(user.player()));
    double cps = clickPerSecond(delays);
    if (cps > 15 && exampleMeta.doubleClicks == 0) {
      Bukkit.broadcastMessage("§cBye:! " + cps + " " + exampleMeta.doubleClicks);
    }
  }

  public static class AirClickLimitBlueprintMeta extends SwingBlueprintMeta {
    // Nothing yet!
  }
}