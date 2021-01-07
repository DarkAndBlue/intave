package de.jpx3.intave.trustfactor;

import de.jpx3.intave.IntavePlugin;
import de.jpx3.intave.access.DefaultForwardingPermissionTrustFactorResolver;
import de.jpx3.intave.access.TrustFactorResolver;
import de.jpx3.intave.event.bukkit.BukkitEventSubscriber;
import de.jpx3.intave.event.bukkit.BukkitEventSubscription;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

public final class TrustFactorService implements BukkitEventSubscriber {
  private final IntavePlugin plugin;

  private TrustFactorResolver trustFactorResolver;
  private TrustFactorConfiguration trustFactorConfiguration;

  public TrustFactorService(IntavePlugin plugin) {
    this.plugin = plugin;
  }

  public void setup() {
    TrustFactorLoader trustFactorLoader = new DownloadingTrustFactorLoader();
    trustFactorConfiguration = trustFactorLoader.fetch();
    trustFactorResolver = new DefaultForwardingPermissionTrustFactorResolver(new DefaultTrustFactorResolver());

    plugin.eventLinker().registerEventsIn(this);
  }

  @BukkitEventSubscription
  public void on(PlayerJoinEvent join) {
    Player player = join.getPlayer();

  }

  public TrustFactorResolver trustFactorResolver() {
    return trustFactorResolver;
  }

  public void setTrustFactorResolver(TrustFactorResolver trustFactorResolver) {
    this.trustFactorResolver = trustFactorResolver;
  }

  public TrustFactorConfiguration trustFactorConfiguration() {
    return trustFactorConfiguration;
  }
}
