package de.jpx3.intave.share;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class HistoryWindow<T> implements Set<T> {
  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  private final Lock readLock = lock.readLock();
  private final Lock writeLock = lock.writeLock();

  private final int capacity;
  private final T[] elements;
  private int pos;
  private long fullSize;

  public HistoryWindow(int capacity) {
    this.capacity = capacity;
    this.elements = (T[]) new Object[capacity];
  }

  @Override
  public int size() {
    try {
      readLock.lock();
      return Math.min(Math.abs((int) fullSize), capacity);
    } finally {
      readLock.unlock();
    }
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public boolean contains(Object o) {
    try {
      readLock.lock();
      for (T element : elements) {
        if (element.equals(o)) {
          return true;
        }
      }
      return false;
    } finally {
      readLock.unlock();
    }
  }

  @Override
  public @NotNull Iterator<T> iterator() {
    // iterate from pos backwards to pos + 1
    return new Iterator<T>() {
      private int index = pos;
      private int remaining = size();

      @Override
      public boolean hasNext() {
        return remaining > 0;
      }

      @Override
      public T next() {
        if (remaining <= 0) {
          throw new IndexOutOfBoundsException();
        }
        remaining--;
        return elements[index = ((index - 1) % capacity)];
      }
    };
  }

  public T back(int length) {
    if (length < 0) {
      throw new IllegalArgumentException("Length must be greater than or equal to 0");
    }
    if (length > size()) {
      throw new IllegalArgumentException("Can not go back more than the size of the history");
    }
    try {
      readLock.lock();
      int index = (pos - length) % capacity;
      while (index < 0) {
        index += capacity;
      }
      return elements[index];
    } finally {
      readLock.unlock();
    }
  }

  @Override
  public @NotNull Object[] toArray() {
    throw new UnsupportedOperationException();
  }

  @Override
  public @NotNull <T1> T1[] toArray(@NotNull T1[] a) {
    throw new UnsupportedOperationException();
  }

  public boolean add(T element) {
    try {
      writeLock.lock();
      fullSize++;
      elements[pos = ((pos + 1) % capacity)] = element;
      return true;
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public boolean remove(Object o) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean containsAll(@NotNull Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean addAll(@NotNull Collection<? extends T> c) {
    try {
      writeLock.lock();
      for (T element : c) {
        fullSize++;
        elements[pos = ((pos + 1) % capacity)] = element;
      }
      return true;
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public boolean retainAll(@NotNull Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeAll(@NotNull Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear() {
    try {
      writeLock.lock();
      for (int i = 0; i < capacity; i++) {
        elements[i] = null;
      }
      pos = 0;
      fullSize = 0;
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public boolean equals(Object o) {
    return false;
  }

  @Override
  public int hashCode() {
    return 0;
  }
}
