package de.jpx3.intave;

import de.jpx3.intave.access.IntaveAccess;
import de.jpx3.intave.access.IntaveColdException;
import de.jpx3.intave.tools.annotate.Natify;

import java.lang.ref.WeakReference;

public final class IntaveAccessor {
  private static transient WeakReference<IntaveAccess> weakAccess;

  @Natify
  public static synchronized boolean loaded() {
    IntavePlugin plugin = IntavePlugin.singletonInstance();
    return plugin != null && plugin.isEnabled() && uncheckedUnsafeAccess() != null;
  }

  @Natify
  public static synchronized WeakReference<IntaveAccess> weakAccess() {
    if (!loaded()) {
      throw new IntaveColdException("Intave offline");
    }
    if(weakAccess == null) {
      weakAccess = new WeakReference<>(uncheckedUnsafeAccess());
    }
    return weakAccess;
  }

  @Natify
  public static synchronized IntaveAccess unsafeAccess() {
    if (!loaded()) {
      throw new IntaveColdException("Intave offline");
    }
    return uncheckedUnsafeAccess();
  }

  @Natify
  private static IntaveAccess uncheckedUnsafeAccess() {
    return null;//IntavePlugin.singletonInstance().intaveAccess();
  }
}
