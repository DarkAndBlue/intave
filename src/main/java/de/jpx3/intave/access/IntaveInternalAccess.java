package de.jpx3.intave.access;

import java.util.List;

public interface IntaveInternalAccess {
  void setPermissionProcessor(IntavePermissionCheck hook);
  IntaveCheckAccess accessCheck(String checkName) throws UnknownCheckException;
  List<String> loadedCheckNames();
  @Deprecated
  void restart();
  void reload();
}