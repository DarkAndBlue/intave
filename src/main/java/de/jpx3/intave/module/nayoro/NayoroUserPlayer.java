package de.jpx3.intave.module.nayoro;

import de.jpx3.intave.shade.Position;
import de.jpx3.intave.shade.Rotation;
import de.jpx3.intave.user.User;

public final class NayoroUserPlayer implements NayoroPlayer{
  private final User user;

  public NayoroUserPlayer(User user) {
    this.user = user;
  }

  @Override
  public void sendMessage(String message) {
    user.player().sendMessage(message);
  }

  @Override
  public int id() {
    return user.player().getEntityId();
  }

  @Override
  public Rotation rotation() {
    return user.meta().movement().rotation();
  }

  @Override
  public Rotation lastRotation() {
    return user.meta().movement().lastRotation();
  }

  @Override
  public Position position() {
    return user.meta().movement().position();
  }
}
