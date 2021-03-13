package de.jpx3.intave.update;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import de.jpx3.intave.tools.CachedResource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public final class VersionList {
  private final List<VersionInformation> content = new ArrayList<>();

  public VersionList() {
  }

  public void setup() {
    CachedResource cachedResource = new CachedResource(
      "license-map",
      "https://intave.de/api/versions.json",
      TimeUnit.DAYS.toMillis(2)
    );
    String raw = String.join("", cachedResource.readLines());
    JsonReader json = new JsonReader(new StringReader(raw));
    json.setLenient(true);
    JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();
    for (JsonElement jsonElement : jsonArray) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      String name = jsonObject.get("name").getAsString();
      String release = jsonObject.get("release").getAsString();
      String status = jsonObject.get("status").getAsString();
      content.add(
        new VersionInformation(
          name, Long.parseLong(release),
          VersionInformation.VersionTypeClassifier.valueOf(status.toUpperCase(Locale.ROOT))
        )
      );
    }
  }

  public VersionInformation versionInformation(String version) {
    for (VersionInformation versionInformation : content) {
      if(versionInformation.version().equalsIgnoreCase(version)) {
        return versionInformation;
      }
    }
    return null;
  }

  public List<VersionInformation> content() {
    return content;
  }
}
