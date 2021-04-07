package de.jpx3.intave.world.collision.patches;

import de.jpx3.intave.tools.wrapper.WrappedAxisAlignedBB;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

public final class BlockFenceGatePatch extends BoundingBoxPatch {
  public BlockFenceGatePatch() {
    super(Material.FENCE_GATE, Material.ACACIA_FENCE_GATE);
  }

  @Override
  public List<WrappedAxisAlignedBB> patch(World world, Player player, Block block, List<WrappedAxisAlignedBB> bbs) {
    return bbs;
  }

  @Override
  public List<WrappedAxisAlignedBB> patch(World world, Player player, Material type, int blockState, List<WrappedAxisAlignedBB> bbs) {
    return bbs;
  }
}