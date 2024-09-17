package de.jpx3.intave.block.shape.voxel;

public interface IndexMerger {
  IndexMerger compileMerge();

  boolean forMergedIndices(IndexConsumer indexConsumer);

  IndexList mergedIndexes();

  int size();

  interface IndexConsumer {
    boolean merge(int firstIndex, int secondIndex, int index);
  }
}
