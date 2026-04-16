package de.jpx3.intave.diagnostic.natives;


import java.util.ArrayList;
import java.util.List;

public final class NativeCheck {
  private static boolean CHECK_ACTIVE = false;
  private static boolean COMPLETED = false;
  private static final List<Runnable> NATIVE_CHECKS = new ArrayList<>();

  public static void registerNative(Runnable runnable) {
    if (COMPLETED) {
      throw new IllegalStateException("Native check already completed");
    }
    NATIVE_CHECKS.add(runnable);
  }

  public static void run() {
    CHECK_ACTIVE = true;
    for (Runnable runnable : NATIVE_CHECKS) {
      try {
        runnable.run();
      } catch (UnsatisfiedLinkError exception) {
        exception.printStackTrace();
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    }
    NATIVE_CHECKS.clear();
    CHECK_ACTIVE = false;
    COMPLETED = true;
  }

  public static boolean checkActive() {
    return CHECK_ACTIVE;
  }
}
