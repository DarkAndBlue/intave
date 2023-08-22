package de.jpx3.intave.block.fluid.next;

import org.bukkit.Material;

public interface LiquidResolver {
  Liquid liquidFrom(Material type, int variantIndex);

  default Liquid select(
    boolean isWater,
    boolean isLava,
    boolean dry,
    boolean falling,
    float height,
    boolean source
  ) {
    if (dry) {
      return Dry.of();
    } else if (isWater) {
      return FlowingWater.ofHeight(height, falling);
    } else if (isLava) {
      return FlowingLava.ofHeight(height, falling);
    }
    return Dry.of();
  }

  default float heightFromLegacyLevel(int level) {
    if (level >= 8) {
      level = 0;
    }
    return (float) (level + 1) / 9.0F;
  }
}
