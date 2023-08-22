package de.jpx3.intave.block.fluid.next;

import de.jpx3.intave.IntavePlugin;
import de.jpx3.intave.access.IntaveInternalException;
import de.jpx3.intave.block.state.BlockStateCache;
import de.jpx3.intave.block.state.ExtendedBlockStateCache;
import de.jpx3.intave.block.variant.BlockVariant;
import de.jpx3.intave.block.variant.BlockVariantRegister;
import de.jpx3.intave.klass.rewrite.PatchyLoadingInjector;
import de.jpx3.intave.share.BlockPosition;
import de.jpx3.intave.share.Position;
import de.jpx3.intave.user.User;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static de.jpx3.intave.adapter.MinecraftVersions.*;

public class Liquids {
  private static final Map<Material, Map<Integer, Liquid>> liquidData = new HashMap<>();
  private static LiquidResolver resolver;
  private static LiquidFlow flow;

  public static void setup() {
    String className;
    if (VER1_18_2.atOrAbove()) {
      className = "de.jpx3.intave.block.fluid.next.v18b2LiquidResolver";
    } else if (VER1_16_0.atOrAbove()) {
      className = "de.jpx3.intave.block.fluid.next.v16LiquidResolver";
    } else if (VER1_14_0.atOrAbove()) {
      className = "de.jpx3.intave.block.fluid.next.v14LiquidResolver";
    } else if (VER1_13_0.atOrAbove()) {
      className = "de.jpx3.intave.block.fluid.next.v13LiquidResolver";
    } else {
      className = "de.jpx3.intave.block.fluid.next.v12LiquidResolver";
    }
    PatchyLoadingInjector.loadUnloadedClassPatched(IntavePlugin.class.getClassLoader(), className);
    try {
      resolver = (LiquidResolver) Class.forName(className).newInstance();
    } catch (Exception exception) {
      throw new IntaveInternalException(exception);
    }

    flow = VER1_13_0.atOrAbove() ? new v13LiquidFlow() : new v8LiquidFlow();

//    int variantCount = 0;
    for (Material value : Material.values()) {
      if (value.isBlock()) {
        boolean anyLiquid = false;
        Map<Integer, Liquid> variants = new HashMap<>();
        for (int variantIndex : BlockVariantRegister.variantIdsOf(value)) {
//          variantCount++;
          Liquid currentLiquid = resolver.liquidFrom(value, variantIndex);
          if (!currentLiquid.isDry()) {
            BlockVariant properties = BlockVariantRegister.uncachedVariantOf(value, variantIndex);
            String propertyString = "{"+properties.propertyNames().stream().map(s -> s + ": " + properties.propertyOf(s)).collect(Collectors.joining(", ")) +"}";
            System.out.println("Found liquid " + currentLiquid + " at " + value + ":" + propertyString);
            anyLiquid = true;
          }
          variants.put(variantIndex, currentLiquid);
        }
        if (anyLiquid) {
          liquidData.put(value, variants);
        }
      }
    }
  }

  public static LiquidFlow liquidFlow() {
    return flow;
  }

  public static boolean canContainLiquid(Material material) {
    return liquidData.containsKey(material);
  }

  public static boolean isLiquid(Material material, int variantIndex) {
    Map<Integer, Liquid> liquidMappings = liquidData.get(material);
    return liquidMappings != null && liquidMappings.containsKey(variantIndex)
      && !liquidMappings.get(variantIndex).isDry();
  }

  @Deprecated
  public static @NotNull Liquid liquidStateOf(Material material, int variant) {
    Map<Integer, Liquid> map = liquidData.get(material);
    if (map == null) {
      return Dry.of();
    }
    Liquid liquid = map.get(variant);
    if (liquid == null) {
      return Dry.of();
    }
    return liquid;
  }

  public static @NotNull Liquid liquidAt(User user, Position position) {
    return liquidAt(user, position.getBlockX(), position.getBlockY(), position.getBlockZ());
  }

  public static @NotNull Liquid liquidAt(User user, Location location) {
    return liquidAt(user, location.getBlockX(), location.getBlockY(), location.getBlockZ());
  }

  public static @NotNull Liquid liquidAt(User user, BlockPosition position) {
    return liquidAt(user, position.getX(), position.getY(), position.getZ());
  }

  public static @NotNull Liquid liquidAt(User user, double x, double y, double z) {
    return liquidAt(user, floor(x), floor(y), floor(z));
  }

  public static @NotNull Liquid liquidAt(User user, int x, int y, int z) {
    BlockStateCache states = user.blockStates();
    Material type = states.typeAt(x, y, z);
    Map<Integer, Liquid> stateMap = liquidData.get(type);
    if (stateMap == null) {
      return Dry.of();
    }
    int variant = states.variantIndexAt(x, y, z);
    Liquid liquid = stateMap.get(variant);
    if (liquid == null) {
      return Dry.of();
    }
    return liquid;
  }

  public static boolean liquidPresentAt(User user, Position position) {
    return liquidPresentAt(user, position.getBlockX(), position.getBlockY(), position.getBlockZ());
  }

  public static boolean liquidPresentAt(User user, Location location) {
    return liquidPresentAt(user, location.getBlockX(), location.getBlockY(), location.getBlockZ());
  }

  public static boolean liquidPresentAt(User user, BlockPosition position) {
    return liquidPresentAt(user, position.getX(), position.getY(), position.getZ());
  }

  public static boolean liquidPresentAt(User user, double x, double y, double z) {
    return liquidPresentAt(user, floor(x), floor(y), floor(z));
  }

  public static boolean liquidPresentAt(User user, int x, int y, int z) {
    ExtendedBlockStateCache states = user.blockStates();
    Material type = states.typeAt(x, y, z);
    Map<Integer, Liquid> stateMap = liquidData.get(type);
    if (stateMap == null) {
      return false;
    }
    int variant = states.variantIndexAt(x, y, z);
    Liquid liquid = stateMap.get(variant);
    if (liquid == null) {
      return false;
    }
    return !liquid.isDry();
  }

  private static int floor(double value) {
    int i = (int) value;
    return value < (double) i ? i - 1 : i;
  }
}
