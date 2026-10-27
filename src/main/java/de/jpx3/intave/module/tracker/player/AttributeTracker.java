package de.jpx3.intave.module.tracker.player;

import ac.intave.samples.event.CombatStateUpdateEvent;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedAttribute;
import de.jpx3.intave.module.Module;
import de.jpx3.intave.module.Modules;
import de.jpx3.intave.module.linker.packet.ListenerPriority;
import de.jpx3.intave.module.linker.packet.PacketSubscription;
import de.jpx3.intave.player.attribute.Attribute;
import de.jpx3.intave.player.attribute.AttributeModifier;
import de.jpx3.intave.user.User;
import de.jpx3.intave.user.UserLocal;
import de.jpx3.intave.user.UserRepository;
import de.jpx3.intave.user.meta.AbilityMetadata;
import de.jpx3.intave.user.meta.MovementMetadata;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static de.jpx3.intave.module.linker.packet.PacketId.Server.UPDATE_ATTRIBUTES;

public final class AttributeTracker extends Module {
  // At twenty attacks per second the attack recharges within a single tick
  private static final double LEGACY_ATTACK_SPEED = 20.0D;
  private final UserLocal<State> states = UserLocal.withInitial(State::new);

  @PacketSubscription(
    priority = ListenerPriority.HIGH,
    packetsOut = {
      UPDATE_ATTRIBUTES
    }
  )
  public void sentAttributes(PacketEvent event) {
    Player player = event.getPlayer();
    User user = UserRepository.userOf(player);
    PacketContainer packet = event.getPacket();
    if (packet.getIntegers().read(0) == player.getEntityId()) {
      StructureModifier<List<WrappedAttribute>> mod = packet.getAttributeCollectionModifier();
      List<WrappedAttribute> attributes = mod.read(0);
      mod.write(0, attributes);
      user.tickFeedback(() -> {
        Boolean modernCombat = modernCombat(user);
        attributes.forEach(attribute -> receivedAttribute(user, attribute));
        if (!Objects.equals(modernCombat, modernCombat(user))) {
          emit(user);
        }
      });
    }
  }

  private void receivedAttribute(User user, WrappedAttribute attribute) {
    AbilityMetadata abilities = user.meta().abilities();
    MovementMetadata movement = user.meta().movement();
    String attributeKey = attribute.getAttributeKey();
    if (abilities.hasAttribute(attributeKey)) {
      Attribute intaveAttribute = Attribute.fromProtocolLib(attribute);
      List<AttributeModifier> intaveAttributes = abilities.modifiersOf(intaveAttribute);
      intaveAttributes.clear();
      Set<AttributeModifier> serverAttributes = intaveAttribute.modifiers();
      if (abilities.findAttribute(attributeKey) == abilities.findAttribute("generic.movementSpeed")) {
        movement.hasSprintSpeed = AbilityMetadata.hasSprintModifier(serverAttributes);
      }
      if (abilities.findAttribute(attributeKey) == abilities.findAttribute("generic.attackSpeed")) {
        states.get(user).attackSpeedReceived = true;
      }
      intaveAttributes.addAll(new HashSet<>(serverAttributes));
      abilities.modifyBaseValue(attributeKey, attribute.getBaseValue());
    }
  }

  public void synchronizeSnapshot(User user) {
    user.tickFeedback(() -> emit(user));
  }

  private void emit(User user) {
    Modules.nayoro().emit(user, new CombatStateUpdateEvent(modernCombat(user)));
  }

  private Boolean modernCombat(User user) {
    return modernCombat(user.meta().abilities(), states.get(user).attackSpeedReceived);
  }

  /** Whether the server advertises an attack cooldown, or null until it sent the attack speed. */
  static Boolean modernCombat(AbilityMetadata abilities, boolean attackSpeedReceived) {
    if (!abilities.hasAttribute("generic.attackSpeed")) {
      // Servers before 1.9 have no attack cooldown
      return false;
    }
    if (!attackSpeedReceived) {
      return null;
    }
    double attackSpeed = abilities.attributeValue("generic.attackSpeed");
    return Double.isFinite(attackSpeed) ? attackSpeed < LEGACY_ATTACK_SPEED : null;
  }

  private static final class State {
    private boolean attackSpeedReceived;
  }
}
