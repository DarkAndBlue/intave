package de.jpx3.intave.world.block;

import de.jpx3.intave.tools.wrapper.WrappedAxisAlignedBB;
import org.bukkit.Material;

import java.util.List;

public abstract class BlockData {
  public abstract Material[] affectedMaterials();

  public abstract WrappedAxisAlignedBB interactionBox(int protocolVersion, int data);

  public abstract List<WrappedAxisAlignedBB> collidingBox(int protocolVersion, int data);
}
