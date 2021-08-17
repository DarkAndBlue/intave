package de.jpx3.intave.detect;

import de.jpx3.intave.module.linker.bukkit.BukkitEventSubscriber;
import de.jpx3.intave.module.linker.packet.PacketEventSubscriber;

/**
 * A combination of a {@link BukkitEventSubscriber} and a {@link PacketEventSubscriber},
 * unifying subscription
 */
public interface EventProcessor extends BukkitEventSubscriber, PacketEventSubscriber {
}