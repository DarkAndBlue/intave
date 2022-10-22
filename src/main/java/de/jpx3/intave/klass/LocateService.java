package de.jpx3.intave.klass;

import de.jpx3.intave.IntavePlugin;
import de.jpx3.intave.annotate.HighOrderService;
import de.jpx3.intave.resource.Resource;

@HighOrderService
public final class LocateService {
  private final IntavePlugin plugin;
  private final String key;

  public LocateService(IntavePlugin plugin) {
    this.plugin = plugin;
    this.key = plugin.getConfig().getString("server-mappings", "online");
  }

  public void setup() {
    if (key.equals("online")) {

    }
  }

  public Resource locateResource() {
    return null;
  }
}
