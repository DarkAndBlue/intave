package de.jpx3.intave.world.collision.garbage;

import de.jpx3.intave.tools.annotate.Nullable;
import de.jpx3.intave.tools.wrapper.WrappedAxisAlignedBB;
import org.bukkit.entity.Player;

import java.util.List;

public interface CollidingBoundingBoxesResolver {
  @Nullable
  List<WrappedAxisAlignedBB> resolveCollidingBoundingBoxes(
    Player player,
    WrappedAxisAlignedBB boundingBox
  );
}