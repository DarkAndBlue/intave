package de.jpx3.intave.module.tracker.player;

import de.jpx3.intave.adapter.MinecraftVersion;
import de.jpx3.intave.adapter.MinecraftVersions;
import de.jpx3.intave.test.FakePlayerFactory;
import de.jpx3.intave.user.meta.AbilityMetadata;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

final class AttributeTrackerTest {
  @AfterEach
  void resetVersion() {
    MinecraftVersion.setCurrent(MinecraftVersions.VER1_21_4);
  }

  @Test
  void legacyServersHaveNoAttackCooldown() {
    MinecraftVersion.setCurrent(MinecraftVersions.VER1_8_0);
    assertEquals(false, AttributeTracker.modernCombat(abilities(), false));
  }

  @Test
  void modernServersAreUnknownUntilTheAttackSpeedIsReceived() {
    MinecraftVersion.setCurrent(MinecraftVersions.VER1_21_4);
    AbilityMetadata abilities = abilities();
    assertNull(AttributeTracker.modernCombat(abilities, false));
    assertEquals(true, AttributeTracker.modernCombat(abilities, true));

    // e.g. OldCombatMechanics
    abilities.modifyBaseValue("generic.attackSpeed", 40.0);
    assertEquals(false, AttributeTracker.modernCombat(abilities, true));
  }

  private static AbilityMetadata abilities() {
    return new AbilityMetadata(FakePlayerFactory.createPlayer((ignored, arguments) -> null));
  }
}