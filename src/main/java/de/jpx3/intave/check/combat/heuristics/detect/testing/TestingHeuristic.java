package de.jpx3.intave.check.combat.heuristics.detect.testing;

import de.jpx3.intave.check.MetaCheckPart;
import de.jpx3.intave.check.combat.Heuristics;
import de.jpx3.intave.module.linker.nayoro.NayoroRelay;
import de.jpx3.intave.module.nayoro.ClickEvent;
import de.jpx3.intave.module.nayoro.NayoroPlayer;
import de.jpx3.intave.user.meta.CheckCustomMetadata;

public final class TestingHeuristic extends MetaCheckPart<Heuristics, TestingHeuristic.ExampleMeta> {
  public TestingHeuristic(Heuristics parent) {
    super(parent, ExampleMeta.class);
  }

  @NayoroRelay
  public void on(NayoroPlayer player, ClickEvent event) {
    player.sendMessage("Clicked!");
  }

  public static class ExampleMeta extends CheckCustomMetadata {

  }
}
