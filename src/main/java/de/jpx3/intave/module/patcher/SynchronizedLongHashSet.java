package de.jpx3.intave.module.patcher;

import de.jpx3.intave.klass.rewrite.PatchyAutoTranslation;
import org.bukkit.craftbukkit.v1_8_R3.util.LongHashSet;

@PatchyAutoTranslation
public final class SynchronizedLongHashSet extends LongHashSet {

  @PatchyAutoTranslation
  SynchronizedLongHashSet() {
    super();
  }

  @Override
  @PatchyAutoTranslation
  public synchronized boolean add(long value) {
    return super.add(value);
  }

  @Override
  @PatchyAutoTranslation
  public synchronized boolean add(int msw, int lsw) {
    return super.add(msw, lsw);
  }

  @Override
  @PatchyAutoTranslation
  public synchronized boolean remove(long value) {
    return super.remove(value);
  }

  @Override
  @PatchyAutoTranslation
  public synchronized void remove(int msw, int lsw) {
    super.remove(msw, lsw);
  }
}
