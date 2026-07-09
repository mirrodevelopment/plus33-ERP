/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.service
 * File              : TaxMetricsRegistry.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxMetricsRegistryController
 * Related Service   : TaxMetricsRegistryService, TaxMetricsRegistryServiceImpl
 * Related Repository: TaxMetricsRegistryRepository
 * Related Entity    : TaxMetricsRegistry
 * Related DTO       : N/A
 * Related Mapper    : TaxMetricsRegistryMapper
 * Related DB Table  : tax_metrics_registrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.tax.service;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxMetricsRegistry}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.service}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Performs the recordCalculation operation in this module.
     *
     * @param providerName the providerName input value
     * @param durationMs the durationMs input value
     */
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

    /**
     * Retrieves diagnostics data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
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

    /**
     * Performs the resetMetrics operation in this module.
     *
     */
    public void resetMetrics() {
        totalRequests.set(0);
        totalDurationMs.set(0);
        maxDurationMs.set(0);
        providerUsage.clear();
        cache.resetCounters();
    }
}