package de.jpx3.intave.detect.checks.combat.heuristics;

import com.comphenix.protocol.events.PacketEvent;
import de.jpx3.intave.detect.IntaveMetaCheckPart;
import de.jpx3.intave.detect.checks.combat.Heuristics;
import de.jpx3.intave.event.packet.ListenerPriority;
import de.jpx3.intave.event.packet.PacketDescriptor;
import de.jpx3.intave.event.packet.PacketSubscription;
import de.jpx3.intave.event.packet.Sender;
import de.jpx3.intave.event.service.entity.WrappedEntity;
import de.jpx3.intave.tools.MathHelper;
import de.jpx3.intave.user.User;
import de.jpx3.intave.user.UserCustomCheckMeta;
import de.jpx3.intave.user.UserMetaAttackData;
import de.jpx3.intave.user.UserMetaMovementData;
import org.bukkit.entity.Player;

public final class RotationAccuracyHeuristic extends IntaveMetaCheckPart<Heuristics, RotationAccuracyHeuristic.RotationAccuracyHeuristicMeta> {
  public RotationAccuracyHeuristic(Heuristics parentCheck) {
    super(parentCheck, RotationAccuracyHeuristicMeta.class);
  }

  @PacketSubscription(
    priority = ListenerPriority.HIGH,
    packets = {
      @PacketDescriptor(sender = Sender.CLIENT, packetName = "POSITION_LOOK"),
      @PacketDescriptor(sender = Sender.CLIENT, packetName = "LOOK")
    }
  )
  public void receiveMovement(PacketEvent event) {
    Player player = event.getPlayer();
    User user = userOf(player);
    User.UserMeta meta = user.meta();
    UserMetaMovementData movementData = meta.movementData();
    UserMetaAttackData attackData = meta.attackData();
    RotationAccuracyHeuristicMeta heuristicMeta = metaOf(player);
    WrappedEntity attackedEntity = attackData.lastAttackedEntity();

    if (attackedEntity != null && attackedEntity.moving(0.2) && attackData.recentlyAttacked(1000)) {
      float yawSpeed = MathHelper.distanceInDegrees(movementData.rotationYaw, movementData.lastRotationYaw);
      float pitchSpeed = MathHelper.distanceInDegrees(movementData.rotationPitch, movementData.lastRotationPitch);

      /*
      1: Yaw Check
       */
      if (yawSpeed > 1.0) {
        float distanceToPerfectYaw = MathHelper.distanceInDegrees(attackData.perfectYaw(), movementData.rotationYaw);

        // Check perfect yaw
        if (distanceToPerfectYaw == 0) {
          String description = "rotated yaw too precisely (0.0)";
          Anomaly anomaly = Anomaly.anomalyOf(Confidence.LIKELY, Anomaly.Type.KILL_AURA, description, Anomaly.AnomalyOption.LIMIT_2);
          parentCheck().saveAnomaly(player, anomaly);
        }

        // Check yaw accuracy
        if (yawSpeed > 3.0) {
          double expectedDifference = 2.0;//Math.min(10, yawSpeed * 0.6);
          heuristicMeta.balanceYawAccuracy += expectedDifference - (distanceToPerfectYaw / 0.8);
          heuristicMeta.balanceYawAccuracy = Math.max(0, heuristicMeta.balanceYawAccuracy);
          int suspiciousLevel = (int) heuristicMeta.balanceYawAccuracy;
          if (suspiciousLevel > 8) {
            String description = "high accuracy rotation yaw vl:" + suspiciousLevel;
            Anomaly anomaly = Anomaly.anomalyOf(Confidence.LIKELY, Anomaly.Type.KILL_AURA, description, Anomaly.AnomalyOption.LIMIT_2);
            parentCheck().saveAnomaly(player, anomaly);
          }

          // player.sendMessage("vl:" + suspiciousLevel + ", " + distanceToPerfectYaw);
        }

        // Check yaw accuracy (other)
        if (distanceToPerfectYaw > 4.0) {
          heuristicMeta.balanceYawAccuracyOther = 0;
        } else if (heuristicMeta.balanceYawAccuracyOther++ > 50) {

          String description = "high accuracy rotation yaw (2) vl:" + heuristicMeta.balanceYawAccuracyOther;
          Anomaly anomaly = Anomaly.anomalyOf(Confidence.MAYBE, Anomaly.Type.KILL_AURA, description, Anomaly.AnomalyOption.LIMIT_2);
          parentCheck().saveAnomaly(player, anomaly);


          heuristicMeta.balanceYawAccuracyOther = 0;
        }
      }

      /*
      2: Pitch Check

       if (pitchSpeed > 1.0) {
        float distanceToPerfectPitch = MathHelper.distanceInDegrees(movementData.rotationPitch, attackData.perfectPitch());

        player.sendMessage("dist:" + MathHelper.distanceInDegrees(distanceToPerfectPitch, 3));

      }
       */
    }
  }

  public final static class RotationAccuracyHeuristicMeta extends UserCustomCheckMeta {
    private double balanceYawAccuracy;
    private double balanceYawAccuracyOther;
  }
}
