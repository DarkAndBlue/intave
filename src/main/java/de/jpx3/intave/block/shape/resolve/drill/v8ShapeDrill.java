package de.jpx3.intave.block.shape.resolve.drill;

import de.jpx3.intave.block.shape.BlockShape;
import de.jpx3.intave.block.shape.BlockShapes;
import de.jpx3.intave.block.shape.resolve.drill.acbbs.v8AlwaysCollidingBoundingBox;
import de.jpx3.intave.block.variant.BlockVariantRegister;
import de.jpx3.intave.klass.rewrite.PatchyAutoTranslation;
import de.jpx3.intave.shade.BoundingBox;
import de.jpx3.intave.shade.link.WrapperConverter;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.List;

@PatchyAutoTranslation
public final class v8ShapeDrill extends AbstractShapeDrill {
  private static final v8AlwaysCollidingBoundingBox ALWAYS_COLLIDING_BOX = new v8AlwaysCollidingBoundingBox();

  @Override
  @PatchyAutoTranslation
  public BlockShape collisionShapeOf(World world, Player player, org.bukkit.Material type, int blockState, int posX, int posY, int posZ) {
    BlockPosition blockposition = new BlockPosition(posX, posY, posZ);
    IBlockData blockData = (IBlockData) BlockVariantRegister.rawVariantOf(type, blockState);//Block.getByCombinedId(type.getId() | (blockState & 0xF) << 12);
    if (blockData == null) {
      return BlockShapes.emptyShape();
    }
    List<AxisAlignedBB> bbs = new ArrayList<>();
    WorldServer worldServer = ((CraftWorld) world).getHandle();
    blockData.getBlock().a(worldServer, blockposition, blockData, ALWAYS_COLLIDING_BOX, bbs, null);
    return translate(bbs);
  }

  @Override
  @PatchyAutoTranslation
  public BlockShape outlineShapeOf(World world, Player player, Material type, int blockState, int posX, int posY, int posZ) {
    BlockPosition blockposition = new BlockPosition(posX, posY, posZ);
    IBlockData blockData = (IBlockData) BlockVariantRegister.rawVariantOf(type, blockState);
    if (blockData == null) {
      return BlockShapes.emptyShape();
    }
    WorldServer worldServer = ((CraftWorld) world).getHandle();
    Block block = blockData.getBlock();
    block.updateShape(worldServer, blockposition);
    AxisAlignedBB boundingBox = block.a(worldServer, blockposition, blockData);
    return boundingBox == null ? BlockShapes.emptyShape() : WrapperConverter.boundingBoxFromAABB(boundingBox);
  }
}
