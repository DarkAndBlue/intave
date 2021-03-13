package de.jpx3.intave.world.permission;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public interface BlockInteractionPermissionCheck {
  boolean hasPermission(Player player, Action action, ItemStack itemStack, Block block, BlockFace blockFace);
}
