package de.jpx3.intave.block.fluid.next;

import de.jpx3.intave.block.variant.BlockVariantRegister;
import org.bukkit.Material;

final class v12LiquidResolver implements LiquidResolver {
  private static final Material STATIONARY_WATER = Material.getMaterial("STATIONARY_WATER");
  private static final Material STATIONARY_LAVA = Material.getMaterial("STATIONARY_LAVA");

  @Override
  public Liquid liquidFrom(Material type, int variantIndex) {
    boolean isWater = type == Material.WATER || type == STATIONARY_WATER;
    boolean isLava = type == Material.LAVA || type == STATIONARY_LAVA;
    if (!isWater && !isLava) {
      return Dry.of();
    }
    int level = levelOf(type, variantIndex);
    float height = heightFromLegacyLevel(level);
    if (isWater) {
      return FlowingWater.ofHeight(height, level >= 8);
    } else {
      return FlowingLava.ofHeight(height, level >= 8);
    }
  }

  private static int levelOf(Material material, int variantIndex) {
    return BlockVariantRegister
      .variantOf(material, variantIndex)
      .propertyOf("level");
  }
}
