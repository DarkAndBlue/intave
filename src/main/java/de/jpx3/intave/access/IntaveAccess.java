package de.jpx3.intave.access;

import org.bukkit.entity.Player;

public interface IntaveAccess {
  PlayerAccess player(Player player);
  IntaveInternalAccess internal();
  ServerAccess server();
}
