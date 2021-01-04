package de.jpx3.intave.access;

import org.bukkit.permissions.Permissible;

public interface IntavePermissionCheck {
  boolean hasPermission(IntavePermission permission, Permissible permissible);
}