package de.jpx3.intave.access;

/**
 * Created by Jpx3 on 03.11.2017.
 */

public enum IntavePermission {
  BYPASS("intave.bypass"),
  COMMAND_USE("intave.command"),
  COMMAND_NOTIFY("intave.command.notify"),
  COMMAND_NOTIFYPUSH("intave.command.notifypush"),
  COMMAND_VERBOSE("intave.command.verbose"),
  COMMAND_RELOAD("intave.command.reload"),
  COMMAND_RESTART("intave.command.restart"),
  COMMAND_BOT("intave.command.bot"),
  COMMAND_MESSAGE("intave.command.message"),
  COMMAND_DEBUG("intave.command.debug"),
  @Deprecated
  COMMAND_BROADCAST("intave.command.broadcast"),
  COMMAND_WAVE("intave.command.wave"),
  COMMAND_GUI("intave.command.gui"),
  COMMAND_RESETVL("intave.command.resetvl"),
  COMMAND_AQUIREREJOINBLOCK("intave.command.rejoinblock"),
  PLAYER_SCORE_LOOKUP("intave.command.playerscorelookup"),
  PLAYER_SCORE_RESET("intave.command.playerscorereset"),
  COMMAND_PROXY("intave.command.proxy"),
  COMMAND_DIAGNOSTICS("intave.command.diagnostics");

  private final String bukkitName;

  IntavePermission(String bukkitName) {
    this.bukkitName = bukkitName;
  }

  public String bukkitName() {
    return bukkitName;
  }
}
