package de.jpx3.intave.shade.link;

import de.jpx3.intave.shade.BlockPosition;
import de.jpx3.intave.shade.BoundingBox;
import de.jpx3.intave.shade.NativeVector;

public final class WrapperConverter {
  private static ClassConverter<BoundingBox> boundingBoxLinker;
  private static ClassConverter<BlockPosition> blockPositionLinker;
  private static ClassConverter<NativeVector> vec3DLinker;

  public static void setup() {
    boundingBoxLinker = BoundingBoxLinkage.resolveBoundingBoxConverter();
    blockPositionLinker = BlockPositionLinkage.resolveBlockPositionConverter();
    vec3DLinker = Vec3DLinkage.resolveVec3DConverter();
  }

  public static BoundingBox boundingBoxFromAABB(Object obj) {
    return boundingBoxLinker.convert(obj);
  }

  public static BlockPosition blockPositionFromNativeBlockPosition(Object obj) {
    return blockPositionLinker.convert(obj);
  }

  public static NativeVector vectorFromVec3D(Object obj) {
    return vec3DLinker.convert(obj);
  }
}