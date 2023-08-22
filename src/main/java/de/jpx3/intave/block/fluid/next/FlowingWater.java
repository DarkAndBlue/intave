package de.jpx3.intave.block.fluid.next;

class FlowingWater implements Liquid {
  private final float height;
  private final boolean falling;

  private FlowingWater(float height, boolean falling) {
    this.height = height;
    this.falling = falling;
  }

  @Override
  public boolean isDry() {
    return false;
  }

  @Override
  public boolean isOfWater() {
    return true;
  }

  @Override
  public boolean isOfLava() {
    return false;
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
    return "FlowingWater{" +
      "height=" + height +
      ", falling=" + falling +
      '}';
  }

  public static FlowingWater ofHeight(float height, boolean falling) {
    return new FlowingWater(height, falling);
  }
}
