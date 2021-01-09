package de.jpx3.intave.world.collision;

import de.jpx3.intave.tools.wrapper.WrappedAxisAlignedBB;
import de.jpx3.intave.tools.wrapper.WrappedMathHelper;
import de.jpx3.intave.user.UserRepository;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Collision {

  public static List<WrappedAxisAlignedBB> resolveCollidingBoundingBoxes(
    Player player,
    WrappedAxisAlignedBB boundingBox
  ) {
//    AbstractCollisionDefaultResolver collisionResolver = ForwardingCBBResolver.collisionResolver();
//    return collisionResolver.collidingBoundingBoxes(player, boundingBox);
    return resolve(player, boundingBox);
  }

  public static List<WrappedAxisAlignedBB> resolve(Player player, WrappedAxisAlignedBB playerBoundingBox) {
    // field access & floor -> no performance

    int i = WrappedMathHelper.floor(playerBoundingBox.minX);
    int j = WrappedMathHelper.floor(playerBoundingBox.maxX + 1.0D);
    int k = WrappedMathHelper.floor(playerBoundingBox.minY);
    int l = WrappedMathHelper.floor(playerBoundingBox.maxY + 1.0D);
    int i1 = WrappedMathHelper.floor(playerBoundingBox.minZ);
    int j1 = WrappedMathHelper.floor(playerBoundingBox.maxZ + 1.0D);

    int ystart = Math.max(k - 1, 0);

    // create new array list
    List<WrappedAxisAlignedBB> resolvedBoundingBoxes = null;
    BoundingBoxAccess boundingBoxAccess = UserRepository.userOf(player).boundingBoxAccess();
    World world = player.getWorld();

    // iterate x chunk pos
    //
    // loop
    // estimated: 1
    // max: 2
    //
    // context
    // estimated: 1
    // max: 2
    for(int chunkx = i >> 4; chunkx <= j - 1 >> 4; ++chunkx) {

      // save chunk x start pos
      int chunkXPos = chunkx << 4;

      // iterate z chunk pos
      //
      // loop
      // estimated: 1
      // max: 2
      //
      // context
      // estimated: 1
      // max: 4
      for(int chunkz = i1 >> 4; chunkz <= j1 - 1 >> 4; ++chunkz) {

        // check if chunk is loaded
        if (world.isChunkLoaded(chunkx, chunkz)) { // needs no performance
          // chunk context
          Chunk chunk = world.getChunkAt(chunkx, chunkz);

          // save chunk z start pos
          int chunkZPos = chunkz << 4;

          // max/min calls will be inlined

          // set start point (if block is outside chunk boundaries, don't access it)
          int xstart = Math.max(i, chunkXPos);
          int zstart = Math.max(i1, chunkZPos);

          // set end point (if block is outside chunk boundaries, don't access it)
          int xend = Math.min(j, chunkXPos + 16);
          int zend = Math.min(j1, chunkZPos + 16);


          // iterate
          for(int x = xstart; x < xend; ++x) {
            for(int z = zstart; z < zend; ++z) {
              for(int y = ystart; y < l; ++y) {

                // context
                // estimated: 8 - 12
                // max: ?

                List<WrappedAxisAlignedBB> resolve = boundingBoxAccess.resolve(chunk, x, y, z);
                if(resolve != null && !resolve.isEmpty()) {
                  if(resolvedBoundingBoxes == null) {
                    resolvedBoundingBoxes = new ArrayList<>(resolve);
                  } else {
                    resolvedBoundingBoxes.addAll(resolve);
                  }
                }
              }
            }
          }
        }
      }
    }
    if(resolvedBoundingBoxes == null) {
      resolvedBoundingBoxes = Collections.emptyList();
    } else {
      // filter invalid
      final Iterator<WrappedAxisAlignedBB> each = resolvedBoundingBoxes.iterator();
      while (each.hasNext()) {
        if (!each.next().intersectsWith(playerBoundingBox)) {
          each.remove();
        }
      }
    }
    return resolvedBoundingBoxes;
  }
}
