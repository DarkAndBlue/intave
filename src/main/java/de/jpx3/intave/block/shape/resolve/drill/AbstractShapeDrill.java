package de.jpx3.intave.block.shape.resolve.drill;

import de.jpx3.intave.block.shape.BlockShape;
import de.jpx3.intave.block.shape.BlockShapes;
import de.jpx3.intave.block.shape.ShapeResolverPipeline;
import de.jpx3.intave.shade.BoundingBox;
import de.jpx3.intave.shade.link.WrapperConverter;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractShapeDrill implements ShapeResolverPipeline {
  protected BlockShape translate(List<?> bbs) {
    if (bbs.isEmpty()) {
      return BlockShapes.emptyShape();
    }
    List<BoundingBox> list = new ArrayList<>();
    for (Object bb : bbs) {
      list.add(WrapperConverter.boundingBoxFromAABB(bb));
    }
    return BlockShapes.shapeOf(list);
  }

  protected BlockShape translateWithOffset(List<?> bbs, int posX, int posY, int posZ) {
    if (bbs.isEmpty()) {
      return BlockShapes.emptyShape();
    }
    List<BoundingBox> list = new ArrayList<>();
    for (Object bb : bbs) {
      list.add(BoundingBox.fromNative(bb).offset(posX, posY, posZ));
    }
    return BlockShapes.shapeOf(list);
  }
}
