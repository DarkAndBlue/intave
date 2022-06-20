package de.jpx3.intave.block.state;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import de.jpx3.intave.shade.Position;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

final class FastBlockStateExpiryCache<K> {
  private final Map<Position, BlockState> located = Maps.newConcurrentMap();
  private final Map<K, BlockState> indexed = Maps.newConcurrentMap();
  private final Set<Position> locations = Sets.newConcurrentHashSet();

  private final Player player;
  private final Function<Position, K> keyer;

  FastBlockStateExpiryCache(Player player, Function<Position, K> keyer) {
    this.player = player;
    this.keyer = keyer;
  }

  public BlockState byKey(K index) {
    return indexed.get(index);
  }

  public void insert(Position position, BlockState blockState) {
    located.put(position, blockState);
    locations.add(position);
    indexed.put(keyer.apply(position), blockState);
  }

  public void remove(K key) {
    indexed.remove(key);
  }

  public boolean replaced(K key) {
    return indexed.containsKey(key);
  }

  public void internalRefresh() {
    for (Position location : locations) {
      BlockState blockState = located.get(location);
      if (blockState == null || blockState.expired()) {
//        player.sendMessage("Refreshed " + location + " " + (blockState == null ? "null" : blockState.age()));
        locations.remove(location);
        located.remove(location);
        indexed.remove(keyer.apply(location));
      }
    }
  }

  public void chunkReset(int chunkXMinPos, int chunkXMaxPos, int chunkZMinPos, int chunkZMaxPos) {
    for (Position location : located.keySet()) {
      if (location.getX() >= chunkXMinPos && location.getX() < chunkXMaxPos &&
        location.getZ() >= chunkZMinPos && location.getZ() < chunkZMaxPos) {
        K key = keyer.apply(location);
        located.remove(location);
        locations.remove(location);
        indexed.remove(key);
      }
    }
  }

  public void clear() {
    located.clear();
    indexed.clear();
    locations.clear();
  }

  public Map<Position, BlockState> located() {
    return located;
  }

  public Map<K, BlockState> indexed() {
    return indexed;
  }

  public Set<Position> locations() {
    return locations;
  }
}
