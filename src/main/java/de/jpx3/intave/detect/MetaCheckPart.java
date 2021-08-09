package de.jpx3.intave.detect;

import de.jpx3.intave.user.User;
import de.jpx3.intave.user.UserRepository;
import de.jpx3.intave.user.meta.CheckCustomMetadata;
import org.bukkit.entity.Player;

public abstract class MetaCheckPart<P extends Check, M extends CheckCustomMetadata> extends CheckPart<P> {
  private final Class<? extends CheckCustomMetadata> metaClass;

  public MetaCheckPart(P parentCheck, Class<M> metaClass) {
    super(parentCheck);
    this.metaClass = metaClass;
  }

  public M metaOf(Player player) {
    return metaOf(UserRepository.userOf(player));
  }

  public M metaOf(User user) {
    //noinspection unchecked
    return (M) user.checkMetadata(metaClass);
  }
}
