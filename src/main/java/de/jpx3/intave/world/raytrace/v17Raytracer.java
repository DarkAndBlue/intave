package de.jpx3.intave.world.raytrace;

import de.jpx3.intave.patchy.annotate.PatchyAutoTranslation;
import de.jpx3.intave.tools.wrapper.WrappedMovingObjectPosition;
import de.jpx3.intave.tools.wrapper.WrappedVector;
import net.minecraft.world.level.RayTrace;
import net.minecraft.world.phys.MovingObjectPositionBlock;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

@PatchyAutoTranslation
public final class v17Raytracer implements Raytracer {
  @Override
//  @PatchyAutoTranslation
  public WrappedMovingObjectPosition raytrace(World world, Player player, WrappedVector eyeVector, WrappedVector targetVector) {
    net.minecraft.world.level.World minecraftWorld = ((CraftWorld) world).getHandle().getMinecraftWorld();
    RayTrace raytraceConfiguration = new RayTrace(
      (Vec3D) eyeVector.convertToNativeVec3(),
      (Vec3D) targetVector.convertToNativeVec3(),
      RayTrace.BlockCollisionOption.b /* OUTLINE */,
      RayTrace.FluidCollisionOption.a /* NONE */,
      ((CraftPlayer) player).getHandle()
    );
    MovingObjectPositionBlock movingObjectPositionBlock = minecraftWorld.rayTrace(raytraceConfiguration);
    return WrappedMovingObjectPosition.fromNativeMovingObjectPosition(movingObjectPositionBlock);
  }
}
