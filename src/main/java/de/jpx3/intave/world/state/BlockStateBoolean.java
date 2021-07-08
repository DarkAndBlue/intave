package de.jpx3.intave.world.state;

public final class BlockStateBoolean extends BlockStateData<Boolean> {
  private Object converter;

  public BlockStateBoolean(String name) {
    super(name);
  }

  @Override
  public void build() {
    this.converter = BlockStateServerBridge.booleanStateOf(name());
  }

  @Override
  public Object convert() {
    return this.converter;
  }

  public static BlockStateBoolean of(String name) {
    return new BlockStateBoolean(name);
  }
}