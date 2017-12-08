package com.notjuststudio.threadsauce;

import com.sun.istack.internal.NotNull;

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

    public boolean doIf(@NotNull final DoFunction<Integer> function, @NotNull final CheckFunction<Integer> checker) {
        lock.writeLock().lock();
        try {
            if (checker.check(this.value)) {
                this.value = function.doWith(this.value);
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean incrementIf(@NotNull final CheckFunction<Integer> checker) {
        return doIf(value -> value++, checker);
    }

    public void decrement() {
        doIf(value -> value--, value -> true);
    }
}
