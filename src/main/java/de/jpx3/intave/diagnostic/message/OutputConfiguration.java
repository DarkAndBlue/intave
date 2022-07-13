package de.jpx3.intave.diagnostic.message;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Predicate;

public final class OutputConfiguration {
  private final UUID owner;
  private PrefixDetail prefixDetail;
  private MessageDetail defaultDetail;
  private MessageSeverity minimumSeverity;
  private final Set<MessageCategory> activeCategories;
  private final Map<MessageCategory, ChatColor> outputColors;
  private final Map<MessageCategory, Predicate<Player>> categoryConstraints;
  private final Map<MessageCategory, MessageDetail> messageDetails;

  OutputConfiguration(
    UUID owner, MessageDetail defaultDetail, MessageSeverity minimumSeverity, PrefixDetail prefixDetail,
    Set<MessageCategory> activeCategories, Map<MessageCategory, ChatColor> outputColors,
    Map<MessageCategory, Predicate<Player>> categoryConstraints,
    Map<MessageCategory, MessageDetail> messageDetails
  ) {
    this.owner = owner;
    this.defaultDetail = defaultDetail;
    this.minimumSeverity = minimumSeverity;
    this.prefixDetail = prefixDetail;
    this.activeCategories = activeCategories;
    this.outputColors = outputColors;
    this.categoryConstraints = categoryConstraints;
    this.messageDetails = messageDetails;
  }

  public UUID owner() {
    return owner;
  }

  public MessageSeverity minimumSeverity() {
    return minimumSeverity;
  }

  public boolean isActive(MessageCategory category) {
    return activeCategories.contains(category);
  }

  public boolean canOutput(MessageCategory category, Player player) {
    return isActive(category) && categoryConstraints.computeIfAbsent(category, k -> v -> true).test(player);
  }

  public void activateCategory(MessageCategory category) {
    activeCategories.add(category);
  }

  public void deactivateCategory(MessageCategory category) {
    activeCategories.remove(category);
  }

  public void addConstraint(MessageCategory category, Predicate<Player> constraint) {
    categoryConstraints.put(category, constraint);
  }

  public void removeConstraint(MessageCategory category) {
    categoryConstraints.put(category, player -> true);
  }

  public void setMinimumSeverity(MessageSeverity severity) {
    minimumSeverity = severity;
  }

  public void setDefaultPrefixDetail(PrefixDetail detail) {
    prefixDetail = detail;
  }

  public PrefixDetail prefixSelector() {
    return prefixDetail;
  }

  public MessageDetail detailOf(MessageCategory category) {
    return messageDetails.getOrDefault(category, defaultDetail);
  }

  public ChatColor colorOf(MessageCategory category) {
    return outputColors.get(category);
  }

  public static Builder builder() {
    return new Builder();
  }

  public void activateAllCategories() {
    activeCategories.addAll(Arrays.asList(MessageCategory.values()));
  }

  public void deactivateAllCategories() {
    activeCategories.clear();
  }

  public void setColor(MessageCategory category, ChatColor color) {
    outputColors.put(category, color);
  }

  public void setDefaultMessageDetail(MessageDetail detail) {
    defaultDetail = detail;
  }

  public void setMessageDetail(MessageCategory category, MessageDetail detail) {
    messageDetails.put(category, detail);
  }

  public static class Builder {
    private UUID owner;
    private MessageDetail defaultDetail = MessageDetail.FULL;
    private MessageSeverity minimumSeverity = MessageSeverity.LOW;
    private PrefixDetail prefixDetail = PrefixDetail.FULL;
    private Set<MessageCategory> activeCategories = new HashSet<>();
    private Map<MessageCategory, ChatColor> outputColors = new HashMap<>();
    private Map<MessageCategory, Predicate<Player>> categoryConstraints = new HashMap<>();
    private Map<MessageCategory, MessageDetail> messageDetails = new HashMap<>();

    public Builder setOwner(UUID owner) {
      this.owner = owner;
      return this;
    }

    public Builder setMinimumSeverity(MessageSeverity minimumSeverity) {
      this.minimumSeverity = minimumSeverity;
      return this;
    }

    public Builder setPrefixDetail(PrefixDetail prefixDetail) {
      this.prefixDetail = prefixDetail;
      return this;
    }

    public Builder setActiveCategories(Set<MessageCategory> activeCategories) {
      this.activeCategories = activeCategories;
      return this;
    }

    public Builder setOutputColors(Map<MessageCategory, ChatColor> outputColors) {
      this.outputColors = outputColors;
      return this;
    }

    public Builder setCategoryConstraints(Map<MessageCategory, Predicate<Player>> categoryConstraints) {
      this.categoryConstraints = categoryConstraints;
      return this;
    }

    public Builder defaultDetailSelect(MessageDetail messageDetail) {
//      this.messageDetails = Arrays.stream(MessageCategory.values())
//        .collect(HashMap::new, (map, category) -> map.put(category, messageDetail), HashMap::putAll);
      defaultDetail = messageDetail;
      return this;
    }

    public Builder setMessageDetails(Map<MessageCategory, MessageDetail> messageDetails) {
      this.messageDetails = messageDetails;
      return this;
    }

    public OutputConfiguration build() {
      categoryConstraints = new HashMap<>(categoryConstraints);
      messageDetails = new HashMap<>(messageDetails);
      outputColors = new HashMap<>(outputColors);
      for (MessageCategory value : MessageCategory.values()) {
        if (!categoryConstraints.containsKey(value)) {
          categoryConstraints.put(value, player -> true);
        }
        if (!messageDetails.containsKey(value) || messageDetails.get(value) == MessageDetail.UNSPECIFIED) {
          messageDetails.put(value, defaultDetail);
        }
        if (!outputColors.containsKey(value)) {
          outputColors.put(value, ChatColor.WHITE);
        }
      }

      return new OutputConfiguration(owner, defaultDetail, minimumSeverity, prefixDetail, activeCategories, outputColors, categoryConstraints, messageDetails);
    }
  }
}
