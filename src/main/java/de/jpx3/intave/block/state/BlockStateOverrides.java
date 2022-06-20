package de.jpx3.intave.block.state;

import org.bukkit.Material;
import org.bukkit.World;

/**
 * The {@link BlockStateOverrides} extends the {@link BlockStateLookup} in the definition of additional functions for overrides.
 * A override temporarily replaces a block cache, making the cache ignore the resolved type.
 * The {@link BlockStateLookup} is eligible to delete overrides after 5 seconds after their initialization.
 *
 * @see BlockStateLookup
 * @see BlockStateCaching
 * @see BlockStateAccess
 */

public interface BlockStateOverrides extends BlockStateLookup {
  /**
   * Retrieves if this position is currently being overridden
   *
   * @param posX the x coordinate of the selected block
   * @param posY the y coordinate of the selected block
   * @param posZ the z coordinate of the selected block
   * @return whether the block is currently in override
   */
  boolean currentlyInOverride(int posX, int posY, int posZ);

  /**
   * Retrieve the blocks override
   *
   * @param posX the x coordinate of the selected block
   * @param posY the y coordinate of the selected block
   * @param posZ the z coordinate of the selected block
   * @return the override's blockshape
   */
  BlockState overrideOf(int posX, int posY, int posZ);

  /**
   * Remove a blocks override
   *
   * @param posX the x coordinate of the selected block
   * @param posY the y coordinate of the selected block
   * @param posZ the z coordinate of the selected block
   */
  void invalidateOverride(int posX, int posY, int posZ);

  int numOfIndexedReplacements();

  int numOfLocatedReplacements();

  /**
   * Override a block at a specific position with a custom type and variant.
   *
   * @param world   the world
   * @param posX    the x coordinate of the selected block
   * @param posY    the y coordinate of the selected block
   * @param posZ    the z coordinate of the selected block
   * @param type    the selected type
   * @param variant the selected variant
   */
  void override(World world, int posX, int posY, int posZ, Material type, int variant);

  /**
   * Remove all overrides in specified chunk boundaries
   *
   * @param chunkXMinPos the min chunk x boundary
   * @param chunkXMaxPos the max chunk x boundary
   * @param chunkZMinPos the min chunk z boundary
   * @param chunkZMaxPos the max chunk z boundary
   */
  void invalidateOverridesInBounds(int chunkXMinPos, int chunkXMaxPos, int chunkZMinPos, int chunkZMaxPos);
}
