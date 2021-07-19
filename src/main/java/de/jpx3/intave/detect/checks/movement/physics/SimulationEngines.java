package de.jpx3.intave.detect.checks.movement.physics;

import de.jpx3.intave.detect.checks.movement.physics.simulator.DefaultMoveSimulator;
import de.jpx3.intave.detect.checks.movement.physics.simulator.ElytraMoveSimulator;
import de.jpx3.intave.detect.checks.movement.physics.simulator.HorseMoveSimulator;

public enum SimulationEngines {
  PLAYER(new DefaultMoveSimulator(), ""),
  ELYTRA(new ElytraMoveSimulator(), "ELYTRA"),
  HORSE(new HorseMoveSimulator(), "HORSE");

  private final SimulationEngine calculationPart;
  private final String debug;

  SimulationEngines(SimulationEngine calculationPart, String debug) {
    this.calculationPart = calculationPart;
    this.debug = debug;
  }

  public SimulationEngine simulator() {
    return calculationPart;
  }

  public String debugPrefix() {
    return debug;
  }
}