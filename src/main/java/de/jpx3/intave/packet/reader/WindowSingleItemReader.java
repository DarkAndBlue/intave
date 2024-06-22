package de.jpx3.intave.packet.reader;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class WindowSingleItemReader extends AbstractPacketReader implements WindowItemReader {
  @Override
  public int windowId() {
    return packet().getIntegers().read(0);
  }

  @Override
  public Map<Integer, ItemStack> itemMap() {
    ItemStack item = packet().getItemModifier().read(0);
    int slot = packet().getIntegers().read(1);
    Map<Integer, ItemStack> map = new HashMap<>();
    map.put(slot, item);
    return map;
  }
}
