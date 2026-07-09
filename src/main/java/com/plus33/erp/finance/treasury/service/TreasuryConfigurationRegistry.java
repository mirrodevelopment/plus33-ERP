/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.service
 * File              : TreasuryConfigurationRegistry.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryConfigurationRegistryController
 * Related Service   : TreasuryConfigurationRegistryService, TreasuryConfigurationRegistryServiceImpl
 * Related Repository: TreasuryConfigurationRegistryRepository
 * Related Entity    : TreasuryConfigurationRegistry
 * Related DTO       : N/A
 * Related Mapper    : TreasuryConfigurationRegistryMapper
 * Related DB Table  : treasury_configuration_registrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.finance.treasury.entity.TreasuryAccountingProfile;
import com.plus33.erp.finance.treasury.entity.TreasuryLimit;
import com.plus33.erp.finance.treasury.entity.TreasuryRiskPolicy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Central registry for all treasury configuration lookups.
 * Abstracts away repository calls and provides a caching-friendly façade
 * used by every treasury service to resolve profiles, limits, and policies.
 */
public interface TreasuryConfigurationRegistry {

    /**
     * Resolves the accounting profile for the given company and profile code.
     * Falls back to the company's default profile when profileCode is null.
     *
     * @throws IllegalStateException if no profile is found
     */
    TreasuryAccountingProfile resolveAccountingProfile(Long companyId, String profileCode);

    /**
     * Returns all active risk policies matching the given category on the given date.
     */
    List<TreasuryRiskPolicy> getActivePolicies(Long companyId, String policyCategory, LocalDate effectiveDate);

    /**
     * Returns the most specific active policy matching category + name + currency.
     * Currency-specific policy takes precedence over generic (null-currency) policy.
     */
    Optional<TreasuryRiskPolicy> findBestMatchingPolicy(
            Long companyId, String policyCategory, String policyName,
            String currencyCode, LocalDate effectiveDate);

    /**
     * Returns all active treasury limits for a company.
     */
    List<TreasuryLimit> getActiveLimits(Long companyId);

    /** Evicts all cached data for the given company (e.g., after configuration update). */
    void evictCache(Long companyId);
}
