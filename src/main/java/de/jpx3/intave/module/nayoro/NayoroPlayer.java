package de.jpx3.intave.module.nayoro;

import de.jpx3.intave.shade.Position;
import de.jpx3.intave.shade.Rotation;

/**
 * Class generated using IntelliJ IDEA
 * Created by Richard Strunk 2022
 */

public interface NayoroPlayer {
  void sendMessage(String message);
  int id();
  Rotation rotation();
  Rotation lastRotation();
  Position position();
}
