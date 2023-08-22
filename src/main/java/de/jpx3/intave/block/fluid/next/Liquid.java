package de.jpx3.intave.block.fluid.next;

public interface Liquid {
  boolean isDry();
  boolean isOfWater();
  boolean isOfLava();
  float height();

//  boolean source();
  boolean falling();

  default boolean similarTo(Liquid other) {
    return isOfWater() == other.isOfWater() && isOfLava() == other.isOfLava(); //&&
//      source() == other.source();
  }
}
