package de.jpx3.intave.analytics;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;

import java.util.Set;

public final class PlaytimeRecorder extends Recorder {
  private long startup = System.currentTimeMillis();
  // in minutes
  private long activePlaytime;
  private long afkPlaytime;

  @Override
  public String name() {
    return "playtime";
  }

  @Override
  public JsonObject asJson() {
    JsonObject json = new JsonObject();
    json.addProperty("lifetime", System.currentTimeMillis() - startup / 1000 / 60);
    json.addProperty("total-active-playtime", activePlaytime);
    json.addProperty("total-afk-playtime", afkPlaytime);
    return json;
  }

  @Override
  public void reset() {
    startup = System.currentTimeMillis();
    activePlaytime = 0;
    afkPlaytime = 0;
  }

  public void incrementActiveMinutesBy(long delta) {
    activePlaytime += delta;
  }

  public void incrementAfkMinutesBy(long delta) {
    afkPlaytime += delta;
  }

  @Override
  public Set<DataCategory> categorySet() {
    return ImmutableSet.of(DataCategory.USAGE);
  }
}
