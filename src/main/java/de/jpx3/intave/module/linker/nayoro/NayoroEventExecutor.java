package de.jpx3.intave.module.linker.nayoro;

import de.jpx3.intave.module.nayoro.NayoroEvent;
import de.jpx3.intave.module.nayoro.NayoroPlayer;

/**
 * Class generated using IntelliJ IDEA
 * Created by Richard Strunk 2022
 */

public interface NayoroEventExecutor {
  void execute(NayoroEventSubscriber subscriber, NayoroPlayer player, NayoroEvent event);
}
