package de.jpx3.intave.check;

import de.jpx3.intave.check.combat.ClickPatterns;
import de.jpx3.intave.user.meta.CheckCustomMetadata;

import static de.jpx3.intave.check.ExampleBlueprint.BlueprintMeta;

public abstract class ExampleBlueprint<M extends BlueprintMeta>
  extends CheckPartBlueprintLayout<ClickPatterns, BlueprintMeta, M> {
  private final int sampleSize;

  public ExampleBlueprint(ClickPatterns parentCheck, Class<M> blueprintMetaClass, int sampleSize) {
    super(parentCheck, blueprintMetaClass);
    this.sampleSize = sampleSize;
  }

  public abstract static class BlueprintMeta extends CheckCustomMetadata {
    public String specialString = "";
  }
}
