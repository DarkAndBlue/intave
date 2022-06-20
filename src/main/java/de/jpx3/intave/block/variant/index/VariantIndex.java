package de.jpx3.intave.block.variant.index;

import de.jpx3.intave.IntavePlugin;
import de.jpx3.intave.adapter.MinecraftVersions;
import de.jpx3.intave.klass.rewrite.PatchyLoadingInjector;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class VariantIndex {
  private static final Indexer INDEXER;

  static {
    String indexerClassName;
    if (MinecraftVersions.VER1_13_0.atOrAbove()) {
      if (MinecraftVersions.VER1_14_0.atOrAbove()) {
        indexerClassName = "de.jpx3.intave.block.variant.index.ModernIndexer";
      } else {
        indexerClassName = "de.jpx3.intave.block.variant.index.AquaticIndexer";
      }
    } else {
      indexerClassName = "de.jpx3.intave.block.variant.index.LegacyIndexer";
    }
    Indexer indexer;
    try {
      Class<Object> indexerClass = PatchyLoadingInjector.loadUnloadedClassPatched(
        IntavePlugin.class.getClassLoader(), indexerClassName
      );
      indexer = (Indexer) indexerClass.newInstance();
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
    INDEXER = indexer;
  }

  public static void indexApplyWithReverse(
    Material material,
    BiConsumer<Material, Map<Object, Integer>> indexApply,
    BiConsumer<Material, Map<Integer, Object>> registerApply
  ) {
    Map<Object, Integer> index = index(material);
    // reverse map
    indexApply.accept(material, index);
    registerApply.accept(material, reversed(index));
  }

  public static Map<Object, Integer> index(Material type) {
    return INDEXER.index(type);
  }

  private static <K, V> Map<V, K> reversed(Map<K, V> map) {
    Map<V, K> reversed = new HashMap<>();
    for (Map.Entry<K, V> entry : map.entrySet()) {
      reversed.put(entry.getValue(), entry.getKey());
    }
    return reversed;
  }
}
