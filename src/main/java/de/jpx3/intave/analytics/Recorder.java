package de.jpx3.intave.analytics;

import com.google.gson.JsonObject;

import java.util.Set;

public abstract class Recorder {
  public abstract String name();
  public abstract JsonObject asJson();
  public abstract void reset();
  public abstract Set<DataCategory> categorySet();

  public boolean isForUsage() {
    return categorySet().contains(DataCategory.USAGE);
  }

  public boolean isForErrors() {
    return categorySet().contains(DataCategory.ERRORS);
  }
}
