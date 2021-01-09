package de.jpx3.intave.world.collision.garbage.cached;

import de.jpx3.intave.tools.annotate.Nullable;
import de.jpx3.intave.tools.wrapper.WrappedAxisAlignedBB;
import org.bukkit.World;

import java.util.List;

public interface BlockBoundingBoxAccess {
  @Nullable
  List<WrappedAxisAlignedBB> resolve(World world, int posX, int posY, int posZ);
}