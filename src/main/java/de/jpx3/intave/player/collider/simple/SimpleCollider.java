package de.jpx3.intave.player.collider.simple;

import de.jpx3.intave.shade.BoundingBox;
import de.jpx3.intave.shade.Motion;
import de.jpx3.intave.user.User;

public interface SimpleCollider {
  @Deprecated
  SimpleColliderResult collide(
    User user, BoundingBox boundingBox,
    double motionX, double motionY, double motionZ
  );

  SimpleColliderResult collide(
    User user, BoundingBox boundingBox, Motion motion
  );
}
