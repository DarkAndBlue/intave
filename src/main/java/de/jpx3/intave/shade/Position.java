package de.jpx3.intave.shade;

public final class Position {
  public double xCoordinate, yCoordinate, zCoordinate;

  public Position() {
  }

  public Position(double xCoordinate, double yCoordinate, double zCoordinate) {
    this.xCoordinate = xCoordinate;
    this.yCoordinate = yCoordinate;
    this.zCoordinate = zCoordinate;
  }

  public double xCoordinate() {
    return xCoordinate;
  }

  public double yCoordinate() {
    return yCoordinate;
  }

  public double zCoordinate() {
    return zCoordinate;
  }

  public int blockX() {
    return floor(xCoordinate);
  }

  public int blockY() {
    return floor(yCoordinate);
  }

  public int blockZ() {
    return floor(zCoordinate);
  }

  private int floor(double num) {
    int floor = (int)num;
    return (double)floor == num ? floor : floor - (int)(Double.doubleToRawLongBits(num) >>> 63);
  }

  public Position with(Motion motion) {
    return new Position(xCoordinate + motion.motionX, yCoordinate + motion.motionY, zCoordinate + motion.motionZ);
  }

  @Override
  public String toString() {
    return "Position{" +
      "x=" + xCoordinate +
      ", y=" + yCoordinate +
      ", z=" + zCoordinate +
      '}';
  }

  public static Position empty() {
    return new Position();
  }
}
