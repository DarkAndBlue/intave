package de.jpx3.intave.check.combat.heuristics.detect.clickpatterns;

import de.jpx3.intave.check.combat.Heuristics;

import java.util.List;

import static de.jpx3.intave.check.combat.heuristics.detect.clickpatterns.ExampleSwingCheck.ExampleMeta;

public final class ExampleSwingCheck extends SwingBlueprint<ExampleMeta> {
  public ExampleSwingCheck(Heuristics parentCheck) {
    super(parentCheck, ExampleMeta.class, 300);
  }

  @Override
  public void check(List<Integer> delays) {

  }

  public static class ExampleMeta extends SwingMeta {

  }
}
