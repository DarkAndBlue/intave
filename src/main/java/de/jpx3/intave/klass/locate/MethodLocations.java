package de.jpx3.intave.klass.locate;

import com.comphenix.protocol.utility.MinecraftVersion;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class MethodLocations {
  private final Iterable<MethodLocation> methodLocations;

  public MethodLocations(Iterable<MethodLocation> methodLocations) {
    this.methodLocations = methodLocations;
  }

  public MethodLocations filterByClassKey(String key) {
    return filter(methodLocation -> methodLocation.classKey().equals(key));
  }

  public MethodLocations filterByMethodKey(String key) {
    return filter(methodLocation -> methodLocation.translatedKey().equals(key));
  }

  public MethodLocations reduceToCurrentVersion() {
    int currentMinecraftVersion = currentMinecraftVersion();
    return filter(methodLocation -> methodLocation.versionMatcher().matches(currentMinecraftVersion));
  }

  private int currentMinecraftVersion() {
    return MinecraftVersion.getCurrentVersion().getMinor();
  }

  public Optional<MethodLocation> findAny() {
    return stream().findAny();
  }

  public MethodLocation findAnyOrNull() {
    return findAny().orElse(null);
  }

  public MethodLocation findAnyOrDefault(Supplier<MethodLocation> supplier) {
    return findAny().orElseGet(supplier);
  }

  public Stream<MethodLocation> stream() {
    return StreamSupport.stream(this.methodLocations.spliterator(), false);
  }

  public MethodLocations filter(Predicate<MethodLocation> predicate) {
    return new MethodLocations(
      stream().filter(predicate).collect(Collectors.toList())
    );
  }

  public static MethodLocations empty() {
    return new MethodLocations(Collections.emptyList());
  }
}
