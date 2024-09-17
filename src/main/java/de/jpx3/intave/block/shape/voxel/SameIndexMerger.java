package de.jpx3.intave.block.shape.voxel;

public final class SameIndexMerger implements IndexMerger {
  private final double[] coords;

  public SameIndexMerger(double[] coords) {
    this.coords = coords;
  }

  @Override
  public IndexMerger compileMerge() {
    return this;
  }

  @Override
  public boolean forMergedIndices(IndexConsumer indexConsumer) {
    int i = this.coords.length - 1;
    for (int index = 0; index < i; index++) {
      if (!indexConsumer.merge(index, index, index)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public IndexList mergedIndexes() {
    return IndexList.of(this.coords);
  }

  @Override
  public int size() {
    return this.coords.length;
  }
}
