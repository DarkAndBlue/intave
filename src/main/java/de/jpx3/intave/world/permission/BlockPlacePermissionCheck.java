package de.jpx3.intave.world.permission;

import org.bukkit.World;
import org.bukkit.entity.Player;

public interface BlockPlacePermissionCheck {
  boolean hasPermission(Player player, World world, boolean mainHand, int blockX, int blockY, int blockZ, int enumDirection, int typeId, byte data);
}
