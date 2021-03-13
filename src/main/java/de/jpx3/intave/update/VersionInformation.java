package de.jpx3.intave.update;

public final class VersionInformation {
  private final String version;
  private final long release;
  private final VersionTypeClassifier typeClassifier;

  public VersionInformation(String version, long release, VersionTypeClassifier typeClassifier) {
    this.version = version;
    this.release = release;
    this.typeClassifier = typeClassifier;
  }

  public String version() {
    return version;
  }

  public long release() {
    return release;
  }

  public VersionTypeClassifier typeClassifier() {
    return typeClassifier;
  }

  public enum VersionTypeClassifier {
    OUTDATED,
    LATEST,
    STABLE,
    INVALID
  }
}
