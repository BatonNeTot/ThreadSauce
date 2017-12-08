package com.notjuststudio.threadsauce;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockInteger {

    private int value;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public LockInteger(int value) {
        this.value = value;
    }

    public int get() {
        lock.readLock().lock();
        try {
            return value;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void set(int value) {
        lock.writeLock().lock();
        try {
            this.value = value;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int ifDoElse(@NotNull final CheckFunction<Integer> checker, @NotNull final DoFunction<Integer> doFunction, @Nullable final DoFunction<Integer> elseFunction) {
        lock.writeLock().lock();
        try {
            if (checker.check(this.value)) {
                this.value = doFunction.doWith(this.value);
            } else if (elseFunction != null) {
                this.value = elseFunction.doWith(this.value);
            }
            return this.value;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int ifDo(@NotNull final CheckFunction<Integer> checker, @NotNull final DoFunction<Integer> function) {
        return ifDoElse(checker, function, null);
    }

    public int doWith(@NotNull final DoFunction<Integer> function) {
        return ifDo(value -> true, function);
    }

    public int incrementIf(@NotNull final CheckFunction<Integer> checker) {
        return ifDo(checker, value -> value++);
    }

    public int decrement() {
        return doWith(value -> value--);
    }
}
