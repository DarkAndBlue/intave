package de.jpx3.intave.event.dispatch;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers;
import de.jpx3.intave.IntavePlugin;
import de.jpx3.intave.detect.EventProcessor;
import de.jpx3.intave.event.bukkit.BukkitEventSubscription;
import de.jpx3.intave.event.packet.*;
import de.jpx3.intave.tools.MathHelper;
import de.jpx3.intave.tools.sync.Synchronizer;
import de.jpx3.intave.tools.wrapper.WrappedEnumDirection;
import de.jpx3.intave.user.UserRepository;
import de.jpx3.intave.world.BlockAccessor;
import de.jpx3.intave.world.collision.BoundingBoxAccess;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import static com.comphenix.protocol.wrappers.EnumWrappers.PlayerDigType.STOP_DESTROY_BLOCK;

public final class BlockActionDispatcher implements EventProcessor {
  private final IntavePlugin plugin;

  // TODO: 01/09/21 prevent invalid chunk access

  public BlockActionDispatcher(IntavePlugin plugin) {
    this.plugin = plugin;
    this.plugin.packetSubscriptionLinker().linkSubscriptionsIn(this);
    this.plugin.eventLinker().registerEventsIn(this);
  }

  @PacketSubscription(
    priority = ListenerPriority.NORMAL,
    packets = {
      @PacketDescriptor(sender = Sender.CLIENT, packetName = "BLOCK_PLACE"),
      @PacketDescriptor(sender = Sender.CLIENT, packetName = "USE_ITEM")
    }
  )
  public void receiveInteraction(PacketEvent event) {
    Player player = event.getPlayer();
    PacketContainer packet = event.getPacket();
    BlockPosition blockPosition = packet.getBlockPositionModifier().readSafely(0);
    if(blockPosition == null) {
      return;
    }
    Integer enumDirection = packet.getIntegers().readSafely(0);
    if(enumDirection == null) {
      enumDirection = packet.getDirections().readSafely(0).ordinal();
    }
    if(enumDirection == 255) {
      return;
    }

    if(event.isCancelled()) {
      return;
    }

    World world = player.getWorld();
    Location blockPlacementLocation = blockPosition.toLocation(world).add(WrappedEnumDirection.getFront(enumDirection).getDirectionVec().convertToBukkitVec());
    Material itemTypeInHand = player.getItemInHand().getType();
    boolean isPlacement = itemTypeInHand != Material.AIR && itemTypeInHand.isBlock();

    if(isPlacement) {
      int blockX = blockPlacementLocation.getBlockX();
      int blockY = blockPlacementLocation.getBlockY();
      int blockZ = blockPlacementLocation.getBlockZ();

      EnumWrappers.Hand hand = packet.getHands().readSafely(0);

      int id = itemTypeInHand.getId();
      byte shape = 0;

      boolean access = plugin.interactionPermissionService()
        .blockPlacePermissionCheck()
        .hasPermission(
          player,
          world,
          hand == null || hand == EnumWrappers.Hand.MAIN_HAND,
          blockX, blockY, blockZ,
          id,
          (byte) 0
        );

      if(access) {
        // add to future bounding boxes

//        player.sendMessage("Block-placement confirmation for " + MathHelper.formatPosition(blockPlacementLocation));

        BoundingBoxAccess boundingBoxAccess = UserRepository.userOf(player).boundingBoxAccess();
        boundingBoxAccess.override(world, blockX, blockY, blockZ, id, shape);

        Synchronizer.synchronize(() -> {
          boundingBoxAccess.invalidate(blockX, blockY, blockZ);
          boundingBoxAccess.invalidateOverride(world, blockX, blockY, blockZ);
        });
      }
    } else {

      // doors etc

    }
  }

  @PacketSubscription(
    priority = ListenerPriority.NORMAL,
    packets = {
      @PacketDescriptor(sender = Sender.CLIENT, packetName = "BLOCK_DIG")
    }
  )
  public void receiveBreak(PacketEvent event) {
    Player player = event.getPlayer();
    PacketContainer packet = event.getPacket();
    BlockPosition blockPosition = packet.getBlockPositionModifier().readSafely(0);
    if(blockPosition == null) {
      return;
    }
    EnumWrappers.PlayerDigType playerDigType = packet.getPlayerDigTypes().readSafely(0);
    if(!(playerDigType == STOP_DESTROY_BLOCK)) {
      return;
    }
    EnumWrappers.Direction direction = packet.getDirections().readSafely(0);
    int enumDirection = direction == null ? 255 : direction.ordinal();
    if(enumDirection == 255) {
      return;
    }

    if(event.isCancelled()) {
      return;
    }

    World world = player.getWorld();
    Location blockBreakLocation = blockPosition.toLocation(world);

    boolean access = plugin.interactionPermissionService()
      .blockBreakPermissionCheck()
      .hasPermission(
        player,
        BlockAccessor.blockAccess(blockBreakLocation)
      );

    Bukkit.broadcastMessage("Block-break confirmation for " + MathHelper.formatPosition(blockBreakLocation) + ": " + access);

    if(access) {
      int blockX = blockBreakLocation.getBlockX();
      int blockY = blockBreakLocation.getBlockY();
      int blockZ = blockBreakLocation.getBlockZ();

      // add to future bounding boxes

      BoundingBoxAccess boundingBoxAccess = UserRepository.userOf(player).boundingBoxAccess();
      boundingBoxAccess.override(world, blockX, blockY, blockZ, 0, (byte) 0);

      Synchronizer.synchronize(() -> {
        boundingBoxAccess.invalidate(blockX, blockY, blockZ);
        boundingBoxAccess.invalidateOverride(world, blockX, blockY, blockZ);
      });
    }
  }

/*  @BukkitEventSubscription
  public void on(BlockPlaceEvent event) {
    if(event.getClass() != BlockPlaceEvent.class) {
      return;
    }

    Player player = event.getPlayer();

    Block block = event.getBlockPlaced();
    World world = block.getWorld();

    int posX = block.getX();
    int posY = block.getY();
    int posZ = block.getZ();

    BoundingBoxAccess boundingBoxAccess = UserRepository.userOf(player).boundingBoxAccess();
    boundingBoxAccess.invalidate(posX, posY, posZ);
    boundingBoxAccess.invalidateOverride(world, posX, posY, posZ);
  }

  @BukkitEventSubscription
  public void on(BlockBreakEvent event) {
    if(event.getClass() != BlockBreakEvent.class) {
      return;
    }

    Player player = event.getPlayer();

    Block block = event.getBlock();
    World world = block.getWorld();

    int posX = block.getX();
    int posY = block.getY();
    int posZ = block.getZ();

    BoundingBoxAccess boundingBoxAccess = UserRepository.userOf(player).boundingBoxAccess();
    boundingBoxAccess.invalidate(posX, posY, posZ);
    boundingBoxAccess.invalidateOverride(world, posX, posY, posZ);
  }*/
}
