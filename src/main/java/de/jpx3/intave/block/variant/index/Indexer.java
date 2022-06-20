package de.jpx3.intave.block.variant.index;

import org.bukkit.Material;

import java.util.Map;
import java.util.function.BiConsumer;

interface Indexer {
  Map<Object, Integer> index(
    Material type
  );
}
