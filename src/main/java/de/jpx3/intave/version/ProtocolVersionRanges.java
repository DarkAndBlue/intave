package de.jpx3.intave.version;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class ProtocolVersionRanges implements Iterable<ProtocolVersionRange> {
  private final Collection<ProtocolVersionRange> versionRanges;

  public ProtocolVersionRanges(List<ProtocolVersionRange> versionRanges) {
    this.versionRanges = versionRanges;
  }

  public Stream<ProtocolVersionRange> stream() {
    return versionRanges.stream();
  }

  public Optional<ProtocolVersionRange> newest() {
    return versionRanges.stream()
      .max(ProtocolVersionRange::compareTo);
  }

  public String byProtocolVersion(int version) {
    ProtocolVersionRange protocolVersionRange =
      versionRanges.stream()
        .filter(range -> range.includes(version))
        .findFirst()
        .orElseGet(() -> {
          Optional<ProtocolVersionRange> newest = newest();
          return newest.orElseGet(() -> new ProtocolVersionRange(Integer.MIN_VALUE, Integer.MAX_VALUE, "error"));
        });
    return protocolVersionRange.version();
  }

  @NotNull
  @Override
  public Iterator<ProtocolVersionRange> iterator() {
    return versionRanges.iterator();
  }

  @Override
  public void forEach(Consumer<? super ProtocolVersionRange> action) {
    versionRanges.forEach(action);
  }

  @Override
  public Spliterator<ProtocolVersionRange> spliterator() {
    return versionRanges.spliterator();
  }

  public int byVersion(String version) {
    return -1;
  }
}
