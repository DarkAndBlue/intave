package de.jpx3.intave.command.stages;

import de.jpx3.intave.IntaveControl;
import de.jpx3.intave.IntavePlugin;
import de.jpx3.intave.annotate.Native;
import de.jpx3.intave.command.CommandStage;
import de.jpx3.intave.command.Optional;
import de.jpx3.intave.command.SubCommand;
import de.jpx3.intave.connect.cloud.Cloud;
import de.jpx3.intave.module.Modules;
import de.jpx3.intave.module.nayoro.Classifier;
import de.jpx3.intave.module.nayoro.Nayoro;
import de.jpx3.intave.module.nayoro.OperationalMode;
import de.jpx3.intave.module.nayoro.event.AttackEvent;
import de.jpx3.intave.module.nayoro.event.EntityRemoveEvent;
import de.jpx3.intave.module.nayoro.event.EntitySpawnEvent;
import de.jpx3.intave.module.nayoro.event.sink.EventSink;
import de.jpx3.intave.user.User;
import de.jpx3.intave.user.UserRepository;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public final class SampleStage extends CommandStage {
  private static SampleStage singletonInstance;

  public SampleStage() {
    super(RootStage.singletonInstance(), "sample");
  }

  @SubCommand(
    selectors = "record",
    usage = "[<target>] [<classifier>] <client> <scenario/cheat>",
    description = "Record players"
  )
  @Native
  public void recordCommand(User user, @Optional Player target, @Optional Classifier classifier, @Optional String client, @Optional String scenario) {
    Nayoro nayoro = Modules.nayoro();
    Player player = user.player();
    if (IntaveControl.GOMME_MODE || !IntaveControl.DISABLE_LICENSE_CHECK && !IntavePlugin.singletonInstance().sibyl().isAuthenticated(player)) {
      player.sendMessage(ChatColor.RED + "This command is not available");
      return;
    }
    if (target == null) {
      target = user.player();
    }
    User targetUser = UserRepository.userOf(target);
    if (nayoro.recordingActiveFor(targetUser)) {
      nayoro.disableRecordingFor(targetUser);
      player.sendMessage(ChatColor.RED + "Recording disabled for " + target.getName());
    } else {
      if (scenario == null) {
        user.player().sendMessage(ChatColor.RED + "Please specify a scenario, usage: /iac cloud [<target>] [<classifier>] <client> <scenario/cheat>");
        return;
      }
      if (classifier == null || classifier == Classifier.UNKNOWN) {
        user.player().sendMessage(ChatColor.RED + "Please specify a valid classifier (CHEAT or LEGIT), usage: /iac cloud [<target>] [<classifier>] <client> <scenario/cheat>");
        return;
      }
      if (client == null) {
        user.player().sendMessage(ChatColor.RED + "Please specify a client, usage: /iac cloud [<target>] [<classifier>] <client> <scenario/cheat>");
        return;
      }
      Cloud cloud = IntavePlugin.singletonInstance().cloud();
      cloud.requestSampleTransmission(target, classifier, scenario, client + "@" + targetUser.meta().protocol().versionString(), classifier1 -> {
        nayoro.enableRecordingFor(targetUser, classifier, OperationalMode.CLOUD_STORAGE);
        player.sendMessage(ChatColor.GREEN + "Recording with label \"" + classifier + "\"/"+scenario+" granted by cloud.");
      });
    }
  }

  @SubCommand(
    selectors = "sinks"
  )
  public void sinksCommand(User user) {
    Nayoro nayoro = Modules.nayoro();
    user.player().sendMessage(ChatColor.GRAY + "Active sinks:");
    for (EventSink eventSink : nayoro.sinksOf(user)) {
      user.player().sendMessage(ChatColor.GRAY + " - " + eventSink.name());
    }
  }

  @SubCommand(
    selectors = "entitycontrol"
  )
  public void entityControlCommand(User user) {
    Nayoro nayoro = Modules.nayoro();
    nayoro.pushSink(user, new EventSink() {
      private final Set<Integer> entities = new HashSet<>();

      @Override
      public void visit(EntitySpawnEvent event) {
        user.player().sendMessage(ChatColor.GRAY + "SPAWN: " + event.id() + " " + event.size() + " " + event.name());
        entities.add(event.id());
      }

      @Override
      public void visit(EntityRemoveEvent event) {
        user.player().sendMessage(ChatColor.GRAY + "REMOVE: " + event.id());
        if (!entities.remove(event.id())) {
          user.player().sendMessage(ChatColor.RED + "Entity " + event.id() + " was not spawned before!");
        }
      }

      @Override
      public void visit(AttackEvent event) {
        user.player().sendMessage(ChatColor.GRAY + "ATTACK: " + event.source() + " -> " + event.target());
        if (!entities.contains(event.target())) {
          user.player().sendMessage(ChatColor.RED + "Entity " + event.target() + " was not spawned before!");
        }
      }

      @Override
      public String name() {
        return "EC/anonymous";
      }
    });
    user.player().sendMessage(ChatColor.GREEN + "Entity control enabled");
  }

  public static SampleStage singletonInstance() {
    if (singletonInstance == null) {
      singletonInstance = new SampleStage();
    }
    return singletonInstance;
  }
}
