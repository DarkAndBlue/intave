package de.jpx3.intave.packet.reader;

import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import de.jpx3.intave.adapter.MinecraftVersions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Inspired from work by lukas81298
// See https://pastebin.com/EuprZGHe for the blueprint
public final class EntityMetadataReader extends EntityReader {
  private static final boolean LEGACY_WATCHABLES = !MinecraftVersions.VER1_19_3.atOrAbove();

  public Object fetchRaw(int requiredIndex) {
    if (LEGACY_WATCHABLES) {
      StructureModifier<List<WrappedWatchableObject>> modifier = packet().getWatchableCollectionModifier();
      List<WrappedWatchableObject> lists = modifier.read(0);
      if (lists == null || lists.isEmpty()) {
        return null;
      }
      if (lists.size() == 1) {
        WrappedWatchableObject onlyElement = lists.get(0);
        if (onlyElement.getIndex() == requiredIndex) {
          return onlyElement.getRawValue();
        }
      }
      for (WrappedWatchableObject watchable : lists) {
        if (watchable.getIndex() == requiredIndex) {
          return watchable.getRawValue();
        }
      }
    } else {
      List<WrappedDataValue> values = metadataValues();
      if (values == null || values.isEmpty()) {
        return null;
      }
      if (values.size() == 1) {
        WrappedDataValue onlyElement = values.get(0);
        if (onlyElement.getIndex() == requiredIndex) {
          return onlyElement.getRawValue();
        }
      }
      for (WrappedDataValue value : values) {
        if (value.getIndex() == requiredIndex) {
          return value.getRawValue();
        }
      }
    }
    return null;
  }

  @Deprecated
  public List<WrappedWatchableObject> legacyMetadataObjects() {
    if (LEGACY_WATCHABLES) {
      return packet().getWatchableCollectionModifier().read(0);
    }
    List<WrappedWatchableObject> list = new ArrayList<>();
    for (WrappedDataValue value : metadataValues()) {
      list.add(convertDataValueToWatchable(value));
    }
    return list;
  }

  @Deprecated
  public void setLegacyMetadataObjects(List<WrappedWatchableObject> watchables) {
    if (LEGACY_WATCHABLES) {
      packet().getWatchableCollectionModifier().write(0, watchables);
    } else {
      packet().getDataValueCollectionModifier().write(0, watchables.stream()
        .map(EntityMetadataReader::convertWatchableToDataValue)
        .collect(Collectors.toList()));
    }
  }

  private List<WrappedDataValue> metadataValues() {
    return packet().getDataValueCollectionModifier().read(0);
  }

  private static WrappedWatchableObject convertDataValueToWatchable(WrappedDataValue value) {
    WrappedDataWatcherObject dataWatcherObject = new WrappedDataWatcherObject(
      value.getIndex(), value.getSerializer()
    );
    return new WrappedWatchableObject(dataWatcherObject, value.getValue());
  }

  private static WrappedDataValue convertWatchableToDataValue(WrappedWatchableObject object) {
    return new WrappedDataValue(
      object.getIndex(),
      object.getWatcherObject().getSerializer(),
      object.getValue()
    );
  }
}