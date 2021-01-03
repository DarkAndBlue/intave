package de.jpx3.intave.detect.checks.combat.heuristics;

import de.jpx3.intave.detect.IntaveCheckPart;
import de.jpx3.intave.detect.checks.combat.Heuristics;

public class ExampleHeuristic extends IntaveCheckPart<Heuristics> {

  public ExampleHeuristic(Heuristics parentCheck) {
    super(parentCheck);

    parentCheck.saveAnomaly(null, new Heuristics.Anomaly("Rotations are following enemy movement too precisely", Heuristics.Confidence.PROBABLE, Heuristics.MiningStrategy.SWAP_EMULATION));
  }


}
