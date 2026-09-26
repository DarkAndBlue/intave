/*
 * Copyright 2026 Intave
 *
 * This software is licensed under the PolyForm Perimeter License 1.0.0.
 * You may use this software for any purpose, except for providing to
 * others any product that competes with the software.
 *
 * A copy of the license is available at:
 *   https://polyformproject.org/licenses/perimeter/1.0.0/
 */

package de.jpx3.intave.check.combat.heuristics.combatpatterns.rotation;

import org.junit.jupiter.api.Test;
import de.jpx3.intave.adapter.MinecraftVersion;
import de.jpx3.intave.adapter.MinecraftVersions;
import de.jpx3.intave.user.UserFactory;
import de.jpx3.intave.user.meta.MovementMetadata;

import static de.jpx3.intave.check.combat.heuristics.combatpatterns.rotation.RotationSnapHeuristic.computeYawMotion;
import static de.jpx3.intave.check.combat.heuristics.combatpatterns.rotation.RotationSnapHeuristic.isRotationSnapDetected;
import static org.junit.jupiter.api.Assertions.*;

class RotationSnapHeuristicTest {
  @Test
  void teleportDiscardsPendingSnapAndSilentMovementEvidence() throws Exception {
    MinecraftVersion.setCurrent(MinecraftVersions.VER1_21_4);
    MovementMetadata movement = UserFactory.createFallback().meta().movement();
    RotationSnapHeuristic.RotationSnapHeuristicMeta meta = new RotationSnapHeuristic.RotationSnapHeuristicMeta();
    double[] motions = (double[]) historyField(meta, "yawMotions");
    RotationSnapHeuristic.KeyStates[] keys = (RotationSnapHeuristic.KeyStates[]) historyField(meta, "silentMovements");
    RotationSnapHeuristic.Tick[] positions = (RotationSnapHeuristic.Tick[]) historyField(meta, "movementAtTick");
    motions[0] = 270;
    motions[1] = 0;
    keys[1] = RotationSnapHeuristic.KeyStates.SILENTMOVE;
    positions[1] = new RotationSnapHeuristic.Tick(0, 64, 0, 0, 0);
    assertTrue(detected(motions[1], motions[0], 0, 8));

    meta.resetRotationHistory(movement);

    assertFalse(detected(motions[1], motions[0], 0, 8));
    // A missing sample cannot supply the quiet tick for a new snap either.
    assertFalse(detected(motions[0], 270, 0, 8));
    assertFalse(motions[1] == 0); // The scaffolding path requires an exact zero.
    assertArrayEquals(new RotationSnapHeuristic.KeyStates[] {
      RotationSnapHeuristic.KeyStates.NONE, RotationSnapHeuristic.KeyStates.NONE
    }, keys);
    assertArrayEquals(new RotationSnapHeuristic.Tick[2], positions);
    assertTrue(detected(0, 270, 0, 8)); // Fresh, complete evidence still detects.
  }

  private Object historyField(RotationSnapHeuristic.RotationSnapHeuristicMeta meta, String name) throws Exception {
    java.lang.reflect.Field field = meta.getClass().getDeclaredField(name);
    field.setAccessible(true);
    return field.get(meta);
  }

  // rotationPacketCounter and the swing/attack-recency gate are not under test here;
  // hold them at values that always satisfy the gate so only yaw motion + teleport ticks vary.
  private static final int DEFAULT_ROTATION_PACKET_COUNTER = 20;
  private static final boolean DEFAULT_RECENT_SWING_OR_ATTACK = true;
  private static final int PAST_TELEPORT_TICKS = 8;

  private boolean detected(double previousYawMotion, double lastYawMotion, double currentYawMotion, int ticksPastTeleport) {
    return isRotationSnapDetected(
      previousYawMotion, lastYawMotion, currentYawMotion,
      DEFAULT_RECENT_SWING_OR_ATTACK, DEFAULT_ROTATION_PACKET_COUNTER, ticksPastTeleport
    );
  }

  @Test
  void yawMotionOfPositiveToPositiveRotation() {
    assertEquals(5.0, computeYawMotion(10f, 15f));
  }

  @Test
  void yawMotionOfNegativeToNegativeRotation() {
    assertEquals(5.0, computeYawMotion(-10f, -15f));
  }

  @Test
  void yawMotionOfPositiveToNegativeRotationCrossingZero() {
    assertEquals(10.0, computeYawMotion(5f, -5f));
  }

  @Test
  void yawMotionOfNegativeToPositiveRotationCrossingZero() {
    assertEquals(10.0, computeYawMotion(-5f, 5f));
  }

  @Test
  void yawMotionIsSymmetric() {
    assertEquals(computeYawMotion(30f, -20f), computeYawMotion(-20f, 30f));
  }

  @Test
  void yawMotionOfNoRotationIsZero() {
    assertEquals(0.0, computeYawMotion(-45f, -45f));
  }

  @Test
  void flagsWhenQuietThenSnapThenSettledWithPositiveYaws() {
    // two ticks ago: quiet (<9), last tick: snap (>40), current tick: settled (<9)
    assertTrue(detected(0, 45, 0, PAST_TELEPORT_TICKS));
  }

  @Test
  void flagsWhenQuietThenSnapThenSettledWithNegativeYaws() {
    // yaw motion is always a magnitude (see computeYawMotion), but the snap can be
    // produced by a rotation from a negative to a positive angle or vice versa.
    double snapMotion = computeYawMotion(-30f, 20f);
    assertTrue(detected(0, snapMotion, 0, PAST_TELEPORT_TICKS));
  }

  @Test
  void doesNotFlagWhenNoPriorQuietTick() {
    // previousYawMotion must be < 9; a player who was already moving their view fast doesn't count.
    assertFalse(detected(15, 45, 0, PAST_TELEPORT_TICKS));
  }

  @Test
  void doesNotFlagWhenSnapIsBelowThreshold() {
    // lastYawMotion must exceed 40 to be considered a snap.
    assertFalse(detected(0, 40, 0, PAST_TELEPORT_TICKS));
  }

  @Test
  void doesNotFlagWhenViewDoesNotSettleAfterSnap() {
    // currentYawMotion must drop back below 9 after the snap.
    assertFalse(detected(0, 45, 9, PAST_TELEPORT_TICKS));
  }

  @Test
  void doesNotFlagExactlyAtTeleportBoundary() {
    assertFalse(detected(0, 45, 0, 7));
  }

  @Test
  void flagsOnceEnoughTicksHavePassedSinceTeleport() {
    assertTrue(detected(0, 45, 0, 8));
  }
}
