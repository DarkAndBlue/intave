package de.jpx3.intave.check.movement.physics.eval;

import de.jpx3.intave.check.movement.physics.Simulation;
import de.jpx3.intave.user.meta.MovementMetadata;

abstract class Evaluator {
  abstract void alterEvaluation(Evaluable evaluationContext, MovementMetadata movement, Simulation simulation);
}