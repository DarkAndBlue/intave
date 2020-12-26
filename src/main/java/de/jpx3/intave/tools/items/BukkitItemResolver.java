package de.jpx3.intave.tools.items;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class BukkitItemResolver {
  public static List<Material> materialsByName(String name) {
    return Arrays.stream(Material.values())
      .filter(material -> material.name().toLowerCase().contains(name.toLowerCase()))
      .collect(Collectors.toList());
  }

  public static Material materialByName(String name) {
    return Arrays.stream(Material.values())
      .filter(material -> material.name().equalsIgnoreCase(name))
      .findFirst()
      .orElse(null);
  }
}