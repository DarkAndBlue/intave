package de.jpx3.intave.diagnostic.message;

import com.google.common.collect.ImmutableMap;
import org.bukkit.ChatColor;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

final class HardcodeConfigurationResolver implements ConfigurationResolver {
  @Override
  public OutputConfiguration of(UUID owner) {
    switch (owner.toString()) {
      case "5ee6db6d-6751-4081-9cbf-28eb0f6cc055":
        return richysConfiguration();


      default:
        return defaultConfiguration();
    }
  }

  private OutputConfiguration defaultConfiguration() {
    return richysConfiguration();
  }

  private OutputConfiguration richysConfiguration() {
    UUID owner = UUID.fromString("5ee6db6d-6751-4081-9cbf-28eb0f6cc055");
    EnumSet<MessageCategory> categories = EnumSet.allOf(MessageCategory.class);
    categories.remove(MessageCategory.SIMFUL);
    categories.remove(MessageCategory.TRUSTSET);
    categories.remove(MessageCategory.SIMFLT);

    Map<MessageCategory, ChatColor> colors = new HashMap<>();
    colors.put(MessageCategory.ATLALI, ChatColor.RED);
    colors.put(MessageCategory.ATRAFLT, ChatColor.RED);
    colors.put(MessageCategory.HERAN, ChatColor.RED);
    colors.put(MessageCategory.SIMFLT, ChatColor.DARK_GRAY);
    colors.put(MessageCategory.SIMFUL, ChatColor.GRAY);
    colors.put(MessageCategory.PKBF, ChatColor.GRAY);
    colors.put(MessageCategory.PKDL, ChatColor.GRAY);
    colors.put(MessageCategory.TRUSTSET, ChatColor.GRAY);

    return OutputConfiguration.builder()
      .setOwner(owner)
      .setMinimumSeverity(MessageSeverity.LOW)
      .setPrefixDetail(PrefixDetail.REDUCED_NO_PREFIX)
      .setActiveCategories(categories)
      .setOutputColors(colors)
      .setCategoryConstraints(
        ImmutableMap.of(
          MessageCategory.ATLALI, player -> true,
          MessageCategory.ATRAFLT, player -> true,
          MessageCategory.HERAN, player -> true,
          MessageCategory.SIMFLT, player -> player.getUniqueId().equals(owner)
        )
      )
      .defaultDetailSelect(MessageDetail.FULL)
      .build();
  }
}
