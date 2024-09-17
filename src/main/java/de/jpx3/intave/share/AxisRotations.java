package de.jpx3.intave.share;

public final class AxisRotations {
  public static AxisRotation inverse(AxisRotation input) {
    if (input == AxisRotation.NONE) {
      return AxisRotation.NONE;
    } else if (input == AxisRotation.FORWARD) {
      return AxisRotation.BACKWARD;
    } else if (input == AxisRotation.BACKWARD) {
      return AxisRotation.FORWARD;
    }
    return AxisRotation.NONE;
  }

  private static final Direction.Axis[] BETTER_AXES = {Direction.Axis.X_AXIS, Direction.Axis.Y_AXIS, Direction.Axis.Z_AXIS};

  public static Direction.Axis cycle(AxisRotation rotation, Direction.Axis axis) {
    if (rotation == AxisRotation.NONE) {
      return axis;
    }
    int sign = rotation == AxisRotation.FORWARD ? 1 : -1;
    return BETTER_AXES[Math.floorMod(axis.ordinal() + sign, 3)];
  }
}
