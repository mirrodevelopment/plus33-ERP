package com.plus33.erp.finance.tax.service;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class TaxMetricsRegistry {

    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong totalDurationMs = new AtomicLong(0);
    private final AtomicLong maxDurationMs = new AtomicLong(0);
    private final Map<String, AtomicLong> providerUsage = new ConcurrentHashMap<>();

    private final TaxConfigurationCache cache;

    public TaxMetricsRegistry(TaxConfigurationCache cache) {
        this.cache = cache;
    }

    public void recordCalculation(String providerName, long durationMs) {
        totalRequests.incrementAndGet();
        totalDurationMs.addAndGet(durationMs);

        long currentMax;
        do {
            currentMax = maxDurationMs.get();
            if (durationMs <= currentMax) {
                break;
            }
        } while (!maxDurationMs.compareAndSet(currentMax, durationMs));

        if (providerName != null) {
            providerUsage.computeIfAbsent(providerName, k -> new AtomicLong(0)).incrementAndGet();
        }
    }

    public Map<String, Object> getDiagnostics() {
        Map<String, Object> metrics = new LinkedHashMap<>();
        long reqs = totalRequests.get();
        metrics.put("totalRequests", reqs);
        metrics.put("totalDurationMs", totalDurationMs.get());
        metrics.put("averageDurationMs", reqs > 0 ? (double) totalDurationMs.get() / reqs : 0.0);
        metrics.put("maxDurationMs", maxDurationMs.get());

        Map<String, Long> providers = new LinkedHashMap<>();
        providerUsage.forEach((k, v) -> providers.put(k, v.get()));
        metrics.put("providerUsage", providers);

        Map<String, Object> cacheStats = new LinkedHashMap<>();
        cacheStats.put("rateHits", cache.getRateHits());
        cacheStats.put("rateMisses", cache.getRateMisses());
        cacheStats.put("ruleHits", cache.getRuleHits());
        cacheStats.put("ruleMisses", cache.getRuleMisses());

        long totalHits = cache.getRateHits() + cache.getRuleHits();
        long totalMisses = cache.getRateMisses() + cache.getRuleMisses();
        long totalLookups = totalHits + totalMisses;
        cacheStats.put("totalHits", totalHits);
        cacheStats.put("totalMisses", totalMisses);
        cacheStats.put("hitRatio", totalLookups > 0 ? (double) totalHits / totalLookups : 0.0);
        metrics.put("cacheStatistics", cacheStats);

        return metrics;
    }

    public void resetMetrics() {
        totalRequests.set(0);
        totalDurationMs.set(0);
        maxDurationMs.set(0);
        providerUsage.clear();
        cache.resetCounters();
    }
}
