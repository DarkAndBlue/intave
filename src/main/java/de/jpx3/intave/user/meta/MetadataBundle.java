package de.jpx3.intave.user.meta;

import de.jpx3.intave.user.User;
import org.bukkit.entity.Player;

public final class MetadataBundle {
  private final ViolationMetadata violationLevelData;
  private final MovementMetadata movementData;
  private final AbilityMetadata abilityData;
  private final EffectMetadata potionData;
  private final ProtocolMetadata clientData;
  private final ConnectionMetadata connectionData;
  private final InventoryMetadata inventoryData;
  private final AttackMetadata attackData;
  private final PunishmentMetadata punishmentData;

  public MetadataBundle(Player player, User user) {
    this.violationLevelData = new ViolationMetadata();
    this.clientData = new ProtocolMetadata(player, user);
    this.abilityData = new AbilityMetadata(player);
    this.potionData = new EffectMetadata(player);
    this.inventoryData = new InventoryMetadata(player);
    this.connectionData = new ConnectionMetadata(player);
    this.movementData = new MovementMetadata(player, user);
    this.attackData = new AttackMetadata(player);
    this.punishmentData = new PunishmentMetadata(player);
  }

  public ViolationMetadata violationLevelData() {
    return violationLevelData;
  }

  public MovementMetadata movementData() {
    return movementData;
  }

  public InventoryMetadata inventoryData() {
    return inventoryData;
  }

  public AbilityMetadata abilityData() {
    return abilityData;
  }

  public EffectMetadata potionData() {
    return potionData;
  }

  public ConnectionMetadata connectionData() {
    return connectionData;
  }

  public ProtocolMetadata protocolData() {
    return clientData;
  }

  public AttackMetadata attackData() {
    return attackData;
  }

  public PunishmentMetadata punishmentData() {
    return punishmentData;
  }

  public void setup() {
    movementData.setup();
  }
}
