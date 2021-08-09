package de.jpx3.intave.detect;

import de.jpx3.intave.user.User;
import de.jpx3.intave.user.UserRepository;
import de.jpx3.intave.user.meta.CheckCustomMetadata;
import org.bukkit.entity.Player;

public abstract class MetaCheck<M extends CheckCustomMetadata> extends Check {
  private final Class<? extends CheckCustomMetadata> metaClass;

  public MetaCheck(String checkName, String configurationName, Class<M> metaClass) {
    super(checkName, configurationName);
    this.metaClass = metaClass;
  }

  protected M metaOf(Player player) {
    return metaOf(UserRepository.userOf(player));
  }

  public M metaOf(User user) {
    //noinspection unchecked
    return (M) user.checkMetadata(metaClass);
  }
}
