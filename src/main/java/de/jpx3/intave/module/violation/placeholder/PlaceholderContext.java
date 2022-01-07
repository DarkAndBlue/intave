package de.jpx3.intave.module.violation.placeholder;

import java.util.HashMap;
import java.util.Map;

public interface PlaceholderContext {
  Map<String, String> replacements();

  default PlaceholderContext merge(PlaceholderContext other) {
    Map<String, String> myReplacements = replacements();
    Map<String, String> otherReplacements = other.replacements();
    if (myReplacements.isEmpty()) {
      return other;
    } else if (otherReplacements.isEmpty()) {
      return this;
    }
    Map<String, String> merge = new HashMap<>();
    merge.putAll(myReplacements);
    merge.putAll(otherReplacements);
    return () -> merge;
  }
}