package de.jpx3.intave.packet.converter;

public enum PlayerAction {
  @Deprecated
  PRESS_SHIFT_KEY,
  @Deprecated
  RELEASE_SHIFT_KEY,
  @Deprecated
  START_SNEAKING,
  @Deprecated
  STOP_SNEAKING,
  STOP_SLEEPING,
  START_SPRINTING,
  STOP_SPRINTING,
  START_RIDING_JUMP,
  STOP_RIDING_JUMP,
  OPEN_INVENTORY,
  START_FALL_FLYING,
  RIDING_JUMP;

  public boolean isStartSneak() {
    switch (this) {
      case START_SNEAKING:
      case PRESS_SHIFT_KEY:
        return true;
    }
    return false;
  }

  public boolean isStopSneak() {
    switch (this) {
      case STOP_SNEAKING:
      case RELEASE_SHIFT_KEY:
        return true;
    }
    return false;
  }

  public boolean isSneakRelated() {
    switch (this) {
      case STOP_SNEAKING:
      case START_SNEAKING:
      case PRESS_SHIFT_KEY:
      case RELEASE_SHIFT_KEY:
        return true;
      default:
        return false;
    }
  }
}
