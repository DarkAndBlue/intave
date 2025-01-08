package de.jpx3.intave.module.mitigate;

import org.bukkit.entity.Player;

import java.util.Set;

interface TeleportApplier {
  void teleport(Player player, double posX, double posY, double posZ, float yaw, float pitch, Set<?> relatives);
}
