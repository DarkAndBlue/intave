package de.jpx3.intave.world.blockshape.resolver.pipeline.patcher;

import com.google.common.collect.Lists;
import de.jpx3.intave.tools.wrapper.WrappedAxisAlignedBB;
import de.jpx3.intave.tools.wrapper.WrappedMathHelper;
import de.jpx3.intave.world.blockaccess.BlockDataAccess;
import de.jpx3.intave.world.blockaccess.BlockTypeAccess;
import de.jpx3.intave.world.blockshape.resolver.BoundingBoxBuilder;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

public final class BlockBambooPatch extends BoundingBoxPatch {
  private final static WrappedAxisAlignedBB COLLISION_BOX = new WrappedAxisAlignedBB(6.5D / 16.0, 0.0D, 6.5D / 16.0, 9.5D / 16.0, 1.0, 9.5D / 16.0);

  @Override
  public List<WrappedAxisAlignedBB> patch(World world, Player player, Block block, List<WrappedAxisAlignedBB> bbs) {
    return patch(world, player, block.getX(), block.getY(), block.getZ(), BlockTypeAccess.typeAccess(block, player), BlockDataAccess.dataAccess(block), bbs);
  }

  @Override
  public List<WrappedAxisAlignedBB> patch(World world, Player player, int posX, int posY, int posZ, Material type, int blockState, List<WrappedAxisAlignedBB> bbs) {
    // Small Bamboos Leaves
    if (bbs.isEmpty()) {
      return bbs;
    }
    long randomCoordinate = WrappedMathHelper.coordinateRandom(posX, 0, posZ);
    double offsetX = ((double) ((float) (randomCoordinate & 15L) / 15.0F) - 0.5D) * 0.5D;
    double offsetZ = ((double) ((float) (randomCoordinate >> 8 & 15L) / 15.0F) - 0.5D) * 0.5D;
    double offsetY = 0.0;
    BoundingBoxBuilder builder = BoundingBoxBuilder.create();
    builder.shape(COLLISION_BOX.offset(offsetX, offsetY, offsetZ));
    return builder.applyAndResolve();
  }

  @Override
  public boolean appliesTo(Material material) {
    String name = material.name();
    Lists.newArrayList(new String[0]);
    return name.contains("BAMBOO");
  }
}