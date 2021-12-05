package de.jpx3.intave.klass.locate;

public final class Locations {
  private final ClassLocations classLocations;
  private final FieldLocations fieldLocations;
  private final MethodLocations methodLocations;

  public Locations(
    ClassLocations classLocations,
    FieldLocations fieldLocations,
    MethodLocations methodLocations
  ) {
    this.classLocations = classLocations;
    this.fieldLocations = fieldLocations;
    this.methodLocations = methodLocations;
  }

  public Locations reduced() {
    return new Locations(
      classLocations.reduceToCurrentVersion(),
      fieldLocations.reduceToCurrentVersion(),
      methodLocations.reduceToCurrentVersion()
    );
  }

  public ClassLocations classLocations() {
    return classLocations;
  }

  public FieldLocations fieldLocations() {
    return fieldLocations;
  }

  public MethodLocations methodLocations() {
    return methodLocations;
  }
}
