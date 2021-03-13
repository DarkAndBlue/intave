package de.jpx3.intave.world.permission;

import de.jpx3.intave.access.player.event.BucketAction;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface BucketActionPermissionCheck {
  boolean hasPermission(Player player, BucketAction bucketAction, Block blockClicked, BlockFace blockFace, Material bucket, ItemStack itemInHand);
}
