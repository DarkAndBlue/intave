package de.jpx3.intave.world.collision.garbage.cached;

import de.jpx3.intave.tools.wrapper.WrappedAxisAlignedBB;
import de.jpx3.intave.user.UserRepository;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public final class PatchForwardingBlockBoundingBoxAccess implements BlockBoundingBoxAccess {
  private final int playerVersion;
  private final BlockBoundingBoxAccess forward;


  public PatchForwardingBlockBoundingBoxAccess(Player player, BlockBoundingBoxAccess forward) {
    this.forward = forward;
    this.playerVersion = UserRepository.userOf(player).meta().clientData().protocolVersion();

  }

  @Override
  public List<WrappedAxisAlignedBB> resolve(World world, int posX, int posY, int posZ) {
    List<WrappedAxisAlignedBB> resolve = forward.resolve(world, posX, posY, posZ);
    return resolve;
  }
}
