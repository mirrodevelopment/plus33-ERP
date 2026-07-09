/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.service
 * File              : TaxConfigurationCache.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxConfigurationCacheController
 * Related Service   : TaxConfigurationCacheService, TaxConfigurationCacheServiceImpl
 * Related Repository: TaxConfigurationCacheRepository
 * Related Entity    : TaxConfigurationCache
 * Related DTO       : N/A
 * Related Mapper    : TaxConfigurationCacheMapper
 * Related DB Table  : tax_configuration_caches
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxConfigurationCache}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.service}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class TaxConfigurationCache {

    private final Map<String, List<TaxRate>> rateCache = new ConcurrentHashMap<>();
    private final Map<String, TaxGroup> ruleCache = new ConcurrentHashMap<>();

    private final AtomicLong rateHits = new AtomicLong(0);
    private final AtomicLong rateMisses = new AtomicLong(0);
    private final AtomicLong ruleHits = new AtomicLong(0);
    private final AtomicLong ruleMisses = new AtomicLong(0);

    /**
     * Retrieves or load rates data from the database.
     *
     * @param categoryId the categoryId input value
     * @param date the date input value
     * @param loader the loader input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
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

    /**
     * Retrieves or load rule data from the database.
     *
     * @return the TaxGroup result
     * @throws ResourceNotFoundException if the entity is not found
     */
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

    /**
     * Performs the invalidate operation in this module.
     *
     */
    public void invalidate() {
        rateCache.clear();
        ruleCache.clear();
    }

    /**
     * Retrieves rate hits data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public long getRateHits() {
        return rateHits.get();
    }

    /**
     * Retrieves rate misses data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public long getRateMisses() {
        return rateMisses.get();
    }

    /**
     * Retrieves rule hits data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public long getRuleHits() {
        return ruleHits.get();
    }

    /**
     * Retrieves rule misses data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public long getRuleMisses() {
        return ruleMisses.get();
    }

    // Setter for testing purposes if required
    /**
     * Performs the resetCounters operation in this module.
     *
     */
    public void resetCounters() {
        rateHits.set(0);
        rateMisses.set(0);
        ruleHits.set(0);
        ruleMisses.set(0);
    }
}