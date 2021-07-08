package de.jpx3.intave.world.state;

import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public final class BlockState {
  public final static class Builder {
    private final List<BlockStateData<?>> blockStates = new ArrayList<>();

    private Builder() {
    }

    public Builder with(BlockStateData<?> retriever) {
      this.blockStates.add(retriever);
      return this;
    }

    public BlockState build() {
      return new BlockState(blockStates);
    }
  }

  public BlockState(List<BlockStateData<?>> blockStates) {
    for (BlockStateData<?> blockState : blockStates) {
      blockState.build();
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  public <T> T valueOf(Block block, BlockStateData<T> blockStateData) {
    return blockStateData.value(block);
  }
}