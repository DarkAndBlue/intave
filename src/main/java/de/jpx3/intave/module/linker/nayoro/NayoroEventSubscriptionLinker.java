package de.jpx3.intave.module.linker.nayoro;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import de.jpx3.intave.IntavePlugin;
import de.jpx3.intave.access.IntaveInternalException;
import de.jpx3.intave.klass.create.IRXClassFactory;
import de.jpx3.intave.lib.asm.Type;
import de.jpx3.intave.module.Module;
import de.jpx3.intave.module.linker.bukkit.BukkitEventSubscriptionLinker;
import de.jpx3.intave.module.nayoro.NayoroEvent;
import de.jpx3.intave.module.nayoro.NayoroPlayer;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class NayoroEventSubscriptionLinker extends Module {
  private final IntavePlugin plugin;
  private int totalLoaded = 0;
  private long totalLoad = 0;
  private final Map<Class<? extends NayoroEvent>, List<NayoroRegisteredListener>> eventListeners = Maps.newHashMap();

  public NayoroEventSubscriptionLinker(IntavePlugin plugin) {
    this.plugin = plugin;
  }

  public int totalLoaded() {
    return totalLoaded;
  }

  public long totalLoad() {
    return totalLoad;
  }

  public void registerEventsIn(NayoroEventSubscriber listener) {
    long start = System.nanoTime();
    eventListeners.putAll(processLinking(listener));
    totalLoad += System.nanoTime() - start;
  }

  public void fireEvent(NayoroPlayer player, NayoroEvent event) {
    eventListeners.get(event.getClass()).forEach(executor -> executor.execute(player, event));
  }

  private Map<Class<? extends NayoroEvent>, List<NayoroRegisteredListener>> processLinking(NayoroEventSubscriber listener) {
    Class<? extends NayoroEventSubscriber> listenerClass = listener.getClass();
    List<Method> methods = ImmutableList.copyOf(listenerClass.getDeclaredMethods());
    Map<Class<? extends NayoroEvent>, List<NayoroRegisteredListener>> ret = Maps.newConcurrentMap();

    int found = 0;
    for (Method method : methods) {
      NayoroRelay relayAnnotation = method.getAnnotation(NayoroRelay.class);
      if (relayAnnotation == null) {
        continue;
      }
      Class<?> checkClass;
      if (
        method.getParameterTypes().length == 2 &&
        method.getParameterTypes()[0].equals(NayoroPlayer.class) &&
          NayoroEvent.class.isAssignableFrom(checkClass = method.getParameterTypes()[1])
      ) {
        if (Modifier.isPrivate(method.getModifiers()) || Modifier.isStatic(method.getModifiers())) {
          throw new IntaveInternalException("Invalid linking for method " + method);
        }
        Class<? extends NayoroEvent> eventClass = checkClass.asSubclass(NayoroEvent.class);
        List<NayoroRegisteredListener> registeredListeners = ret.computeIfAbsent(eventClass, k -> new ArrayList<>());
        String playerClassPath = canonicalRepresentation(className(NayoroPlayer.class));
        String concreteListenerClassPath = canonicalRepresentation(className(listenerClass));
        String concreteEventClassPath = canonicalRepresentation(className(eventClass));
        String eventListenerClassPath = canonicalRepresentation(className(NayoroEventSubscriber.class));
        String eventClassPath = canonicalRepresentation(className(NayoroEvent.class));
        Class<NayoroEventExecutor> executorClass = IRXClassFactory.assembleCallerClass(
          BukkitEventSubscriptionLinker.class.getClassLoader(),
          NayoroEventExecutor.class,
          "<generated>",
          "execute",
          "(L"+eventListenerClassPath+";L"+playerClassPath+";L"+eventClassPath+";)V",
          "(L"+concreteListenerClassPath+";L"+ concreteEventClassPath +";)V",
          concreteListenerClassPath,
          method.getName(),
          Type.getMethodDescriptor(method),
          false,
          false
        );
        NayoroEventExecutor executor;
        try {
          executor = executorClass.newInstance();
        } catch (InstantiationException | IllegalAccessException exception) {
          throw new IntaveInternalException(exception);
        }
        NayoroRegisteredListener registeredListener = new NayoroRegisteredListener(
          listener, executor
        );
        registeredListener.initialize();
        registeredListeners.add(registeredListener);
        found++;
      }
    }

    totalLoaded += found;
    return ret;
  }

  private String className(Class<?> clazz) {
    return clazz.getCanonicalName();
  }

  private String canonicalRepresentation(String input) {
    return input.replaceAll("\\.", "/");
  }

  public void disable() {
    eventListeners.clear();
  }
}
