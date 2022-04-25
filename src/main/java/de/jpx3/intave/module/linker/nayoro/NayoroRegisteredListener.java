package de.jpx3.intave.module.linker.nayoro;

import de.jpx3.intave.module.nayoro.NayoroEvent;
import de.jpx3.intave.module.nayoro.NayoroPlayer;

public final class NayoroRegisteredListener {
  private final NayoroEventSubscriber subscriber;
  private final NayoroEventExecutor eventExecutor;

  public NayoroRegisteredListener(NayoroEventSubscriber subscriber, NayoroEventExecutor eventExecutor) {
    this.subscriber = subscriber;
    this.eventExecutor = eventExecutor;
  }

  public void execute(NayoroPlayer player, NayoroEvent event) {
    eventExecutor.execute(subscriber, player, event);
  }

  public void initialize() {

  }
}
