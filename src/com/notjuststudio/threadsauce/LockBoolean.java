package com.notjuststudio.threadsauce;

import com.sun.istack.internal.NotNull;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockBoolean {

    private boolean value;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public LockBoolean(@NotNull final boolean value) {
        this.value = value;
    }

    public boolean get() {
        lock.readLock().lock();
        try {
            return value;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void set(@NotNull final boolean value) {
        lock.writeLock().lock();
        try {
            this.value = value;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean getAndSet(@NotNull final boolean value) {
        lock.writeLock().lock();
        try {
            return this.value;
        } finally {
            this.value = value;
            lock.writeLock().unlock();
        }
    }
}
