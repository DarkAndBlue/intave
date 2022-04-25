package de.jpx3.intave.module.nayoro;

import de.jpx3.intave.module.tracker.entity.EntityShade;
import de.jpx3.intave.shade.Position;
import de.jpx3.intave.user.User;

import java.util.List;

public final class LiveEnvironment implements Environment {
  private final User user;
  private final NayoroUserPlayer player;

  public LiveEnvironment(User user) {
    this.user = user;
    this.player = new NayoroUserPlayer(user);
  }

  @Override
  public NayoroPlayer mainPlayer() {
    return player;
  }

  @Override
  public List<Integer> entities() {
    return user.meta().connection().entityIds();
  }

  @Override
  public Position positionOf(int id) {
    EntityShade.EntityPositionContext position = user.meta().connection().entityBy(id).position;
    return new Position(position.posX, position.posY, position.posZ);
  }

  @Override
  public long duration() {
    return -1;
  }

  @Override
  public long time() {
    return -1;
  }
}
