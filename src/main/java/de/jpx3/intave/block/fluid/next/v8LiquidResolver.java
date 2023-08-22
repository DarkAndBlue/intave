package de.jpx3.intave.block.fluid.next;

import org.bukkit.Material;

final class v8LiquidResolver implements LiquidResolver {
  private static final Material STATIONARY_WATER = Material.getMaterial("STATIONARY_WATER");
  private static final Material STATIONARY_LAVA = Material.getMaterial("STATIONARY_LAVA");

  @Override
  public Liquid liquidFrom(Material type, int variantIndex) {
    boolean isWater = type == Material.WATER || type == STATIONARY_WATER;
    boolean isLava = type == Material.LAVA || type == STATIONARY_LAVA;
    if (!isWater && !isLava) {
      return Dry.of();
    }
    float height = heightFromLegacyLevel(variantIndex);
    if (isWater) {
      return FlowingWater.ofHeight(height, variantIndex >= 8);
    } else {
      return FlowingLava.ofHeight(height, variantIndex >= 8);
    }
  }
}
