package com.plus33.erp.integration.resilience;

import java.util.concurrent.Semaphore;

public class Bulkhead {
    private final String name;
    private final Semaphore semaphore;

    public Bulkhead(String name, int maxConcurrentCalls) {
        this.name = name;
        this.semaphore = new Semaphore(maxConcurrentCalls);
    }

    public boolean tryAcquire() {
        return semaphore.tryAcquire();
    }

    public void release() {
        semaphore.release();
    }

    public String getName() { return name; }
}