package de.jpx3.intave.block.shape;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface ShapeResolverPipeline {
  BlockShape collisionShapeOf(World world, Player player, Material type, int variantIndex, int posX, int posY, int posZ);

  BlockShape outlineShapeOf(World world, Player player, Material type, int variantIndex, int posX, int posY, int posZ);

  default void downstreamTypeReset(Material type) {
  }
}
