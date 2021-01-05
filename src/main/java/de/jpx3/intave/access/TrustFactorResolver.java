package de.jpx3.intave.access;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

public abstract class TrustFactorResolver {
  public abstract void resolveTrustFactor(Player player, Consumer<TrustFactor> callback);
}
