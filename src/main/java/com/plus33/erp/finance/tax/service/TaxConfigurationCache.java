package com.plus33.erp.finance.tax.service;

import com.plus33.erp.finance.tax.entity.TaxRate;
import com.plus33.erp.finance.tax.entity.TaxGroup;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

@Component
public class TaxConfigurationCache {

    private final Map<String, List<TaxRate>> rateCache = new ConcurrentHashMap<>();
    private final Map<String, TaxGroup> ruleCache = new ConcurrentHashMap<>();

    private final AtomicLong rateHits = new AtomicLong(0);
    private final AtomicLong rateMisses = new AtomicLong(0);
    private final AtomicLong ruleHits = new AtomicLong(0);
    private final AtomicLong ruleMisses = new AtomicLong(0);

    public List<TaxRate> getOrLoadRates(Long categoryId, LocalDate date, Supplier<List<TaxRate>> loader) {
        String key = categoryId + ":" + date;
        if (rateCache.containsKey(key)) {
            rateHits.incrementAndGet();
            return rateCache.get(key);
        } else {
            rateMisses.incrementAndGet();
            List<TaxRate> rates = loader.get();
            rateCache.put(key, rates);
            return rates;
        }
    }

    public TaxGroup getOrLoadRule(
            Long companyId,
            String docType,
            String custProfile,
            String suppProfile,
            String prodCat,
            String originCountry,
            String originState,
            String destCountry,
            String destState,
            String incoterms,
            LocalDate date,
            Supplier<TaxGroup> loader
    ) {
        String key = String.format("%d:%s:%s:%s:%s:%s:%s:%s:%s:%s:%s",
                companyId,
                docType != null ? docType : "",
                custProfile != null ? custProfile : "",
                suppProfile != null ? suppProfile : "",
                prodCat != null ? prodCat : "",
                originCountry != null ? originCountry : "",
                originState != null ? originState : "",
                destCountry != null ? destCountry : "",
                destState != null ? destState : "",
                incoterms != null ? incoterms : "",
                date != null ? date.toString() : ""
        );
        if (ruleCache.containsKey(key)) {
            ruleHits.incrementAndGet();
            return ruleCache.get(key);
        } else {
            ruleMisses.incrementAndGet();
            TaxGroup group = loader.get();
            ruleCache.put(key, group);
            return group;
        }
    }

    public void invalidate() {
        rateCache.clear();
        ruleCache.clear();
    }

    public long getRateHits() {
        return rateHits.get();
    }

    public long getRateMisses() {
        return rateMisses.get();
    }

    public long getRuleHits() {
        return ruleHits.get();
    }

    public long getRuleMisses() {
        return ruleMisses.get();
    }

    // Setter for testing purposes if required
    public void resetCounters() {
        rateHits.set(0);
        rateMisses.set(0);
        ruleHits.set(0);
        ruleMisses.set(0);
    }
}
