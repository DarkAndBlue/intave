package de.jpx3.intave.world.collision;

import de.jpx3.intave.tools.wrapper.WrappedAxisAlignedBB;
import de.jpx3.intave.user.User;
import de.jpx3.intave.user.UserMetaMovementData;
import org.bukkit.Material;

import java.util.Collections;
import java.util.List;

public final class ScaffoldingCollisionModifier extends CollisionModifier {
  @Override
  public List<WrappedAxisAlignedBB> modify(User user, WrappedAxisAlignedBB userBox, int posX, int posY, int posZ, List<WrappedAxisAlignedBB> boxes) {
    if (useCustomCollision(user, posY)) {
      double yStart = 14.0 / 16.0;
      double yEnd = 1.0;
      return Collections.singletonList(WrappedAxisAlignedBB.fromBounds(
        posX, posY + yStart, posZ,
        posX + 1, posY + yEnd, posZ + 1
      ));
    } else {
      return Collections.emptyList();
    }
  }

  private boolean useCustomCollision(User user, int blockY) {
    UserMetaMovementData movementData = user.meta().movementData();
    return movementData.positionY >= blockY + 1 - (double) 0.00001f;
  }

  @Override
  public boolean matches(Material material) {
    String name = material.name();
    return name.contains("SCAFFOLDING");
  }
}
