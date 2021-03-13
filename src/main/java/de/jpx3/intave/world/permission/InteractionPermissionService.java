package de.jpx3.intave.world.permission;

import de.jpx3.intave.IntavePlugin;

public final class InteractionPermissionService {
  private final IntavePlugin plugin;
  private BlockPlacePermissionCheck blockPlacePermissionCheck;
  private BlockBreakPermissionCheck blockBreakPermissionCheck;
  private BucketActionPermissionCheck bucketActionPermissionCheck;

  public InteractionPermissionService(IntavePlugin plugin) {
    this.plugin = plugin;
    setup();
  }

  public void setup() {
    blockPlacePermissionCheck = new EventPlacePermissionResolver(plugin);
    blockBreakPermissionCheck = new EventBreakPermissionResolver(plugin);
    bucketActionPermissionCheck = new EventBukkitActionPermissionResolver(plugin);
  }

  public BlockPlacePermissionCheck blockPlacePermissionCheck() {
    return blockPlacePermissionCheck;
  }

  public BlockBreakPermissionCheck blockBreakPermissionCheck() {
    return blockBreakPermissionCheck;
  }

  public BucketActionPermissionCheck bucketActionPermissionCheck() {
    return bucketActionPermissionCheck;
  }
}
