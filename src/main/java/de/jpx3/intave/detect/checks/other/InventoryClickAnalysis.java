package de.jpx3.intave.detect.checks.other;

import de.jpx3.intave.IntaveControl;
import de.jpx3.intave.IntavePlugin;
import de.jpx3.intave.adapter.ProtocolLibAdapter;
import de.jpx3.intave.detect.IntaveMetaCheck;
import de.jpx3.intave.event.bukkit.BukkitEventSubscription;
import de.jpx3.intave.event.punishment.AttackCancelType;
import de.jpx3.intave.event.service.violation.Violation;
import de.jpx3.intave.event.service.violation.ViolationContext;
import de.jpx3.intave.tools.AccessHelper;
import de.jpx3.intave.tools.MathHelper;
import de.jpx3.intave.user.User;
import de.jpx3.intave.user.UserCustomCheckMeta;
import de.jpx3.intave.user.UserMetaInventoryData;
import de.jpx3.intave.user.UserRepository;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public final class InventoryClickAnalysis extends IntaveMetaCheck<InventoryClickAnalysis.ICAData> {
  private final IntavePlugin plugin;

  private final boolean invalidVersion;

  public InventoryClickAnalysis(IntavePlugin plugin) {
    super("InventoryClickAnalysis", "inventoryclickanalysis", ICAData.class);
    this.plugin = plugin;

    invalidVersion = ProtocolLibAdapter.serverVersion().isAtLeast(ProtocolLibAdapter.EXPLORATION_UPDATE);
  }

  @BukkitEventSubscription
  public void receiveWindowClick(InventoryClickEvent event) {
    HumanEntity whoClicked = event.getWhoClicked();
    if (!(whoClicked instanceof Player)) {
      return;
    }
    Player player = ((Player) whoClicked).getPlayer();
    User user = UserRepository.userOf(player);
    User.UserMeta meta = user.meta();
    UserMetaInventoryData inventoryData = meta.inventoryData();
    boolean inventoryOpen = inventoryData.inventoryOpen();

    ClickType click = event.getClick();
    if (click == ClickType.CREATIVE) {
      return;
    }

    if (inventoryData.forceInventoryOnClickOpen && !inventoryOpen) {
      String message = "insufficient inventory-click (inventory not open)";
      Violation violation = Violation.fromType(InventoryClickAnalysis.class)
        .withPlayer(player).withMessage(message)
        .withVL(1)
        .build();
      plugin.violationProcessor().processViolation(violation);
      event.setCancelled(true);
    }
  }

  @BukkitEventSubscription(priority = EventPriority.LOWEST)
  public void on2(InventoryClickEvent event) {
    Player player = (Player) event.getWhoClicked();

    if (player.getGameMode().equals(GameMode.CREATIVE) || invalidVersion || event.getCurrentItem() == null) {
      return;
    }

    ICAData meta = metaOf(player);

    if (meta.lastClickedType != null && meta.lastClickedType.equals(event.getCurrentItem().getType())) {
      return;
    }

    int slot = event.getRawSlot();
    int lastSlot = meta.lastClickedSlot;

    double time = (AccessHelper.now() - meta.lastClickInv) / 1000d;
    double distance = distanceBetween(slot, lastSlot);

    double speedAttr = distance / time;

    boolean flag = speedAttr > 30;
    boolean flag2 = speedAttr > 100;

    if (distance > 2 && flag && (flag2 || AccessHelper.now() - meta.lastTimeEstimatedMousePositonMovedTooQuickly < 5000)) {
      Violation violation = Violation.fromType(InventoryClickAnalysis.class)
        .withPlayer(player).withDefaultThreshold()
        .withMessage("is moving estimated mouse position too quickly between item slots")
        .withDetails(MathHelper.formatDouble(distance, 3) + " slots in " + MathHelper.formatDouble(time, 3) + " seconds")
        .withVL(1).build();

      ViolationContext violationContext = plugin.violationProcessor().processViolation(violation);

//      if(violationContext.shouldCounterThreat()) {
//        event.setCancelled(true);
//      }

      if(IntaveControl.GOMME_MODE) {
        if(distance > 0) {
          plugin.eventService().attackCancelService().requestDamageCancel(userOf(player), AttackCancelType.MEDIUM);
        }
      }
    }

    if (flag) {
      meta.lastTimeEstimatedMousePositonMovedTooQuickly = AccessHelper.now();
    }

    meta.lastClickedSlot = slot;
    meta.lastClickInv = AccessHelper.now();
    meta.lastClickedType = event.getCurrentItem().getType();
  }

  private double distanceBetween(int slot1, int slot2) {
    int[] slot1XZ = translatePosition(slot1);
    int[] slot2XZ = translatePosition(slot2);

    return Math.sqrt((slot1XZ[0] - slot2XZ[0]) * (slot1XZ[0] - slot2XZ[0]) + (slot1XZ[1] - slot2XZ[1]) * (slot1XZ[1] - slot2XZ[1]));
  }

  private int[] translatePosition(int slot) {
    int row = (slot / 9) + 1;
    int rowPosition = slot - ((row - 1) * 9);
    return new int[]{row, rowPosition};
  }

  public static class ICAData extends UserCustomCheckMeta {
    public Material lastClickedType;
    public int lastClickedSlot;
    public long lastClickInv;
    public long lastTimeEstimatedMousePositonMovedTooQuickly;
  }
}