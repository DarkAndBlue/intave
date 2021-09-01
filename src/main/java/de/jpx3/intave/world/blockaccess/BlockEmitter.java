package de.jpx3.intave.world.blockaccess;

import de.jpx3.intave.adapter.MinecraftVersions;
import de.jpx3.intave.reflect.patchy.PatchyLoadingInjector;
import de.jpx3.intave.reflect.patchy.annotate.PatchyAutoTranslation;
import de.jpx3.intave.reflect.patchy.annotate.PatchyTranslateParameters;
import de.jpx3.intave.user.User;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.GeneratorAccess;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R2.CraftChunk;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;

public final class BlockEmitter {
  public static void setup() {
    ClassLoader classLoader = BlockEmitter.class.getClassLoader();
    PatchyLoadingInjector.loadUnloadedClassPatched(classLoader, "de.jpx3.intave.world.blockaccess.BlockEmitter$InternalEmitter");
    if (MinecraftVersions.VER1_13_0.atOrAbove()) {
      PatchyLoadingInjector.loadUnloadedClassPatched(classLoader, "de.jpx3.intave.world.blockaccess.BlockEmitter$v13EmittedBlock");
    } else {
      PatchyLoadingInjector.loadUnloadedClassPatched(classLoader, "de.jpx3.intave.world.blockaccess.BlockEmitter$v8EmittedBlock");
    }

  }

  public static Block emit(User user, Block input) {
    return InternalEmitter.emit(user, input.getChunk(), input.getX(), input.getY(), input.getZ());
  }

  @PatchyAutoTranslation
  public static class InternalEmitter {
    @PatchyAutoTranslation
    public static Block emit(User user, Chunk chunk, int x, int y, int z) {
      if (MinecraftVersions.VER1_13_0.atOrAbove()) {
        return new v13EmittedBlock(user, ((CraftWorld) ((CraftChunk) chunk).getWorld()).getHandle(), x, y, z);
      } else {
        return new v8EmittedBlock(user, (org.bukkit.craftbukkit.v1_8_R3.CraftChunk) chunk, x, y, z);
      }
    }
  }

  @PatchyAutoTranslation
  public static class v8EmittedBlock extends org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock {
    private final User user;
    private final Location location;

    @PatchyAutoTranslation
    @PatchyTranslateParameters
    public v8EmittedBlock(User user, org.bukkit.craftbukkit.v1_8_R3.CraftChunk chunk, int x, int y, int z) {
      super(chunk, x, y, z);
      this.user = user;
    }

    {
      location = getLocation();
    }

    @Override
    public Material getType() {
      return BukkitBlockAccess.cacheAppliedTypeAccess(user, location);
    }
  }

  @PatchyAutoTranslation
  public static class v13EmittedBlock extends org.bukkit.craftbukkit.v1_13_R2.block.CraftBlock {
    private final User user;
    private final Location location;

    @PatchyAutoTranslation
    @PatchyTranslateParameters
    public v13EmittedBlock(User user, Object access, int x, int y, int z) {
      super((GeneratorAccess) access, new BlockPosition(x, y, z));
      this.user = user;
    }

    {
      location = getLocation();
    }

    @Override
    public Material getType() {
      return BukkitBlockAccess.cacheAppliedTypeAccess(user, location);
    }
  }
}
