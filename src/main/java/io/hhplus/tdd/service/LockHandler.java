package io.hhplus.tdd.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockHandler {

    private Map<Long, Lock> lockMap = new ConcurrentHashMap<>();

    public <T> T executeOnLock(Long key, Block<T> block) {
        Lock lock = lockMap.computeIfAbsent(key, k -> new ReentrantLock());
        boolean acquired;
        try {
            acquired = lock.tryLock(5, TimeUnit.SECONDS);
            if (!acquired) throw new RuntimeException("타임에러");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            return block.apply();
        } finally {
            lock.unlock();
        }
    }

    interface Block<T> {
        T apply();
    }
}
