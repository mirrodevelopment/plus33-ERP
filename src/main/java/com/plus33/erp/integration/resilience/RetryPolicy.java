package com.plus33.erp.integration.resilience;

public class RetryPolicy {
    private String name;
    private String type = "EXPONENTIAL"; // e.g. FIXED, LINEAR, EXPONENTIAL
    private int maxAttempts = 3;
    private long backoffMs = 1000;
    private double multiplier = 2.0;

    public RetryPolicy() {}

    public RetryPolicy(String name, String type, int maxAttempts, long backoffMs, double multiplier) {
        this.name = name;
        this.type = type;
        this.maxAttempts = maxAttempts;
        this.backoffMs = backoffMs;
        this.multiplier = multiplier;
    }

    public long calculateDelay(int attempt) {
        if ("FIXED".equalsIgnoreCase(type)) {
            return backoffMs;
        } else if ("LINEAR".equalsIgnoreCase(type)) {
            return backoffMs * attempt;
        } else {
            return (long) (backoffMs * Math.pow(multiplier, attempt - 1));
        }
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getMaxAttempts() { return maxAttempts; }
    public void setMaxAttempts(int maxAttempts) { this.maxAttempts = maxAttempts; }
    public long getBackoffMs() { return backoffMs; }
    public void setBackoffMs(long backoffMs) { this.backoffMs = backoffMs; }
    public double getMultiplier() { return multiplier; }
    public void setMultiplier(double multiplier) { this.multiplier = multiplier; }
}