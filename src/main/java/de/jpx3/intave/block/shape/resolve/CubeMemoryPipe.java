package de.jpx3.intave.block.shape.resolve;

import de.jpx3.intave.block.shape.BlockShape;
import de.jpx3.intave.block.shape.BlockShapes;
import de.jpx3.intave.block.shape.ShapeResolverPipeline;
import de.jpx3.intave.diagnostic.ShapeAccessFlowStudy;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Set;

final class CubeMemoryPipe implements ShapeResolverPipeline {
  private final ShapeResolverPipeline forward;
  private final CubeMemory collisionCubeMemory = new CubeMemory();
  private final CubeMemory outlierCubeMemory = new CubeMemory();

  public CubeMemoryPipe(ShapeResolverPipeline forward) {
    this.forward = forward;
    this.preloadBlocks();
  }

  public void preloadBlocks() {
    for (Material type : Material.values()) {
      String typeName = type.name();
      if (typeName.contains("SLAB") /* can be doubled */) {
        collisionCubeMemory.rememberCubic(type);
      }
    }
  }

  @Override
  public BlockShape collisionShapeOf(World world, Player player, Material type, int variantIndex, int posX, int posY, int posZ) {
    CubeMemory memory = collisionCubeMemory;
    if (memory.knownToBeNonCubic(type)) {
      ShapeAccessFlowStudy.incremDynamic();
      return BlockShapes.cubeAt(posX, posY, posZ);
    } else if (memory.knownToBeNonCubic(type)) {
      return forward.collisionShapeOf(world, player, type, variantIndex, posX, posY, posZ);
    }
    BlockShape shape = forward.collisionShapeOf(world, player, type, variantIndex, posX, posY, posZ);
    if (isInLoadedChunk(world, posX, posZ)) {
      if (shape.isCubic()) {
        downstreamTypeReset(type); // flush downstream type
        memory.rememberCubic(type);
      } else {
        memory.rememberNonCubic(type);
      }
    }
    return shape;
  }

  @Override
  public BlockShape outlineShapeOf(World world, Player player, Material type, int variantIndex, int posX, int posY, int posZ) {
    CubeMemory memory = outlierCubeMemory;
    if (memory.knownToBeNonCubic(type)) {
      ShapeAccessFlowStudy.incremDynamic();
      return BlockShapes.cubeAt(posX, posY, posZ);
    } else if (memory.knownToBeNonCubic(type)) {
      return forward.outlineShapeOf(world, player, type, variantIndex, posX, posY, posZ);
    }
    BlockShape shape = forward.outlineShapeOf(world, player, type, variantIndex, posX, posY, posZ);
    if (isInLoadedChunk(world, posX, posZ)) {
      if (shape.isCubic()) {
        downstreamTypeReset(type); // flush downstream type
        memory.rememberCubic(type);
      } else {
        memory.rememberNonCubic(type);
      }
    }
    return shape;
  }

  @Override
  public void downstreamTypeReset(Material type) {
    collisionCubeMemory.reset(type);
    outlierCubeMemory.reset(type);
    forward.downstreamTypeReset(type);
  }

  public static boolean isInLoadedChunk(World world, int x, int z) {
    return world.isChunkLoaded(x >> 4, z >> 4);
  }

  public static class CubeMemory {
    private final Set<Material> cubes = UnsafeCopyOnWriteEnumSet.of(Material.class);
    private final Set<Material> nonCubes = UnsafeCopyOnWriteEnumSet.of(Material.class);

    public void reset(Material type) {
      cubes.remove(type);
      nonCubes.remove(type);
    }

    public void rememberCubic(Material type) {
      cubes.add(type);
      nonCubes.remove(type);
    }

    public void rememberNonCubic(Material type) {
      cubes.remove(type);
      nonCubes.add(type);
    }

    public boolean knownToBeCubic(Material type) {
      return cubes.contains(type);
    }

    public boolean knownToBeNonCubic(Material type) {
      return nonCubes.contains(type);
    }
  }
}