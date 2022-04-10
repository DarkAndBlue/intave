package de.jpx3.intave.check;

import de.jpx3.intave.check.combat.ClickPatterns;

import static de.jpx3.intave.check.ExampleCheckPart.OurAutoClickerMeta;

public final class ExampleCheckPart extends ExampleBlueprint<OurAutoClickerMeta> {
  public ExampleCheckPart(ClickPatterns parentCheck) {
    super(parentCheck, OurAutoClickerMeta.class, 4);
  }

  public void execute() {
    OurAutoClickerMeta meta = metaOf(userOf(null));
    meta.specialString = "";
  }

  public static class OurAutoClickerMeta extends BlueprintMeta {
    public String test;
  }
}
