package de.jpx3.intave.world.collision.garbage;

import de.jpx3.intave.tools.wrapper.WrappedAxisAlignedBB;
import de.jpx3.intave.world.collision.garbage.forward.AbstractCollisionDefaultResolver;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public final class GlobalCollisionResolver {
  private static final List<String> blockIntersectionExclusionNames = Arrays.asList(
    "ANVIL", "CARPET", "CARROT", "CHEST", "CROPS", "ENDER_CHEST", "GRASS_PATH",
    "IRON_FENCE", "KELP_PLANT", "LADDER", "LILY_PAD", "PISTON_MOVING_PIECE",
    "POTATO", "SEAGRASS", "SOIL", "TALL_SEAGRASS", "TRAPPED_CHEST", "VINE",
    "WATER", "WATER_LILY", "WHEAT"
  );

  public static List<String> blockIntersectionExclusionNames() {
    return blockIntersectionExclusionNames;
  }
}
