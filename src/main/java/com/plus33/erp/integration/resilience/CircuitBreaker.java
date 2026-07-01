package com.plus33.erp.integration.resilience;

import java.time.LocalDateTime;

public class CircuitBreaker {
    private String name;
    private String state = "CLOSED"; // CLOSED, OPEN, HALF_OPEN
    private int failureThreshold = 5;
    private long cooldownMs = 5000;
    private int failureCount = 0;
    private LocalDateTime openTime;

    public CircuitBreaker(String name, int failureThreshold, long cooldownMs) {
        this.name = name;
        this.failureThreshold = failureThreshold;
        this.cooldownMs = cooldownMs;
    }

    public synchronized boolean allowCall() {
        if ("OPEN".equals(state)) {
            if (openTime != null && LocalDateTime.now().isAfter(openTime.plusNanos(cooldownMs * 1_000_000L))) {
                state = "HALF_OPEN";
                return true;
            }
            return false;
        }
        return true;
    }

    public synchronized void recordSuccess() {
        failureCount = 0;
        state = "CLOSED";
        openTime = null;
    }

    public synchronized void recordFailure() {
        failureCount++;
        if (failureCount >= failureThreshold) {
            state = "OPEN";
            openTime = LocalDateTime.now();
        }
    }

    // Getters
    public String getName() { return name; }
    public String getState() { return state; }
    public int getFailureCount() { return failureCount; }
}