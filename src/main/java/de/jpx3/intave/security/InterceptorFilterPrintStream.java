package de.jpx3.intave.security;

import java.io.OutputStream;
import java.io.PrintStream;

public final class InterceptorFilterPrintStream extends PrintStream {
  public static boolean foundInterceptor = false;

  public InterceptorFilterPrintStream(OutputStream out) {
    super(out);
  }

  @Override
  public void println(String input) {
    if (input != null && input.startsWith("[Interceptor]")) {
      foundInterceptor = true;
    }
    super.println(input);
  }
}
