package de.jpx3.intave.block.fluid.next;

class FlowingLava implements Liquid {
  private final float height;
  private final boolean falling;

  private FlowingLava(float height, boolean falling) {
    this.height = height;
    this.falling = falling;
  }

  @Override
  public boolean isDry() {
    return false;
  }

  @Override
  public boolean isOfWater() {
    return false;
  }

  @Override
  public boolean isOfLava() {
    return true;
  }

  @Override
  public float height() {
    return height;
  }

  @Override
  public boolean falling() {
    return falling;
  }

  @Override
  public String toString() {
    return "FlowingLava{" +
      "height=" + height +
      ", falling=" + falling +
      '}';
  }

  public static FlowingLava ofHeight(float height, boolean falling) {
    return new FlowingLava(height, falling);
  }
}
