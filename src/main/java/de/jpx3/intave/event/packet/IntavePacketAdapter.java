package de.jpx3.intave.event.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import org.bukkit.plugin.Plugin;

public abstract class IntavePacketAdapter extends PacketAdapter {
  public IntavePacketAdapter(Plugin plugin, PacketType... types) {
    super(plugin, types);
  }

  public IntavePacketAdapter(Plugin plugin, ListenerPriority listenerPriority, Iterable<? extends PacketType> types) {
    super(plugin, listenerPriority, types);
  }

  public IntavePacketAdapter(Plugin plugin, ListenerPriority listenerPriority, PacketType... types) {
    super(plugin, listenerPriority, types);
  }

  public void tryRemovePluginReference() {
    plugin = null;
  }
}
