package de.jpx3.intave.world.block;

public final class BlockBoxResolver {

  // version << 16 | id << 8 | data, boxes
/*
  private final static WrappedAxisAlignedBB[][] boundingBoxes = new WrappedAxisAlignedBB[4096 * 127][];
  private final static AxisAlignedBB ALWAYS_COLLIDING_BOX = new AxisAlignedBB(0, 0, 0, 0, 0, 0) {
    @Override
    public boolean b(AxisAlignedBB axisAlignedBB) {
      return true;
    }
  };
*/

/*  public static void setup() {
    System.out.println("Loading boxes..");
    long start = System.nanoTime();

    World world = Bukkit.getWorlds().get(0);
    int boxes = 0;
    for (int blockId = 0; blockId < 4096; blockId++) {
      for (int blockState = 0; blockState < 1 << 4; blockState++) {
        WrappedAxisAlignedBB[] wrappedAxisAlignedBBS = resolveBBsOf(world, blockId, (byte) blockState).toArray(new WrappedAxisAlignedBB[0]);
        if(wrappedAxisAlignedBBS.length > 0) {
          boxes += (boundingBoxes[blockId << 4 | blockState] = wrappedAxisAlignedBBS).length;
        }
      }
    }
    long duration = System.nanoTime() - start;

    System.out.println("Loaded " + boxes + " boxes in " + duration + "ns");
  }*/

/*  private static List<WrappedAxisAlignedBB> resolveBBsOf(World world, int blockId, byte blockState) {
    IBlockData blockData = Block.d.a(blockId << 4 | blockState);
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
  }*/



/*  public WrappedAxisAlignedBB interactionBoundingBoxOf(int protocolVersion, Block block) {
    throw new UnsupportedOperationException();
  }

  public List<WrappedAxisAlignedBB> collidingBoundingBoxesOf(int protocolVersion, Block block) {
    throw new UnsupportedOperationException();
  }

  public List<WrappedAxisAlignedBB> collidingBoundingBoxesOf(int protocolVersion, Location location, Material material, byte data) {
    throw new UnsupportedOperationException();
  }*/
}
