package de.jpx3.intave.block.fluid.next;

import de.jpx3.intave.share.BoundingBox;
import de.jpx3.intave.share.Motion;
import de.jpx3.intave.user.User;

public interface LiquidFlow {
  boolean applyFlow(User user, BoundingBox boundingBox);

  Motion pushMotionAt(User user, int blockX, int blockY, int blockZ);
}
