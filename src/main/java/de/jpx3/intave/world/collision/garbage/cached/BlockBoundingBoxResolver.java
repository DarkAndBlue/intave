package de.jpx3.intave.world.collision.garbage.cached;

import de.jpx3.intave.tools.wrapper.WrappedAxisAlignedBB;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Chunk;
import net.minecraft.server.v1_8_R3.IBlockData;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class BlockBoundingBoxResolver implements BlockBoundingBoxAccess {
  private final static AxisAlignedBB ALWAYS_COLLIDING_BOX = new AxisAlignedBB(0, 0, 0, 0, 0, 0) {
    @Override
    public boolean b(AxisAlignedBB axisAlignedBB) {
      return true;
    }
  };

  @Override
  public List<WrappedAxisAlignedBB> resolve(World world, int posX, int posY, int posZ) {
    Chunk handle = ((CraftChunk) world.getChunkAt(posX >> 4, posZ >> 4)).getHandle();

    IBlockData blockData = handle.getBlockData(new BlockPosition(posX, posY, posZ));
    List<AxisAlignedBB> bbs = new ArrayList<>();
    if(blockData == null) {
      return Collections.emptyList();
    }
    try {
      blockData.getBlock().a(
        ((CraftWorld) world).getHandle(),
        BlockPosition.ZERO,
        blockData,
        ALWAYS_COLLIDING_BOX,
        bbs,
        null
      );
    } catch (IllegalArgumentException ignored) {
      return Collections.emptyList();
    }
    if(bbs.isEmpty()) {
      return Collections.emptyList();
    }
    List<WrappedAxisAlignedBB> list = new ArrayList<>();
    for (AxisAlignedBB bb : bbs) {
      list.add(WrappedAxisAlignedBB.fromClass(bb));
    }
    return list;
  }
}
