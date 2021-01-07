package de.jpx3.intave.trustfactor;

import de.jpx3.intave.IntavePlugin;
import de.jpx3.intave.tools.CachedResource;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public final class DownloadingTrustFactorLoader implements TrustFactorLoader {
  @Override
  public TrustFactorConfiguration fetch() {
    CachedResource trustfactor = new CachedResource("trustfactor", "https://intave.de/api/trustfactor/" + IntavePlugin.version() + ".yml", TimeUnit.DAYS.toMillis(7));
    trustfactor.prepareFile();
    return new YamlTrustFactorConfiguration(YamlConfiguration.loadConfiguration(new InputStreamReader(trustfactor.read())));
  }
}
