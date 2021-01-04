package de.jpx3.intave.access;

/**
 * Class generated using IntelliJ IDEA
 * Created by Richard Strunk 2021
 */

public enum TrustFactor {
  BYPASS(1000, "intave.bypass"), // pocketmc
  DARK_GREEN(2, "intave.trust.darkgreen"), // badlion
  GREEN(1, "intave.trust.green"), // labymod / playtime
  YELLOW(0, "intave.trust.yellow"), // default
  ORANGE(-1, "intave.trust.orange"), // once banned
  RED(-2, "intave.trust.red"), // recently banned
  DARK_RED(-3, "intave.trust.darkred") // invis installed / alt account

  ;

  final int factor;
  final String permission;

  TrustFactor(int factor, String permission) {
    this.factor = factor;
    this.permission = permission;
  }
}
