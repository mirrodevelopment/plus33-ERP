/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.service
 * File              : TreasuryConfigurationRegistryImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryConfigurationRegistryController
 * Related Service   : TreasuryConfigurationRegistryImpl
 * Related Repository: TreasuryAccountingProfileRepository, TreasuryRiskPolicyRepository
 * Related Entity    : TreasuryConfigurationRegistry
 * Related DTO       : N/A
 * Related Mapper    : TreasuryConfigurationRegistryMapper
 * Related DB Table  : treasury_configuration_registrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TreasuryConfigurationRegistryController, TreasuryConfigurationRegistryImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements TreasuryConfigurationRegistryService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.finance.treasury.entity.TreasuryAccountingProfile;
import com.plus33.erp.finance.treasury.entity.TreasuryLimit;
import com.plus33.erp.finance.treasury.entity.TreasuryRiskPolicy;
import com.plus33.erp.finance.treasury.repository.TreasuryAccountingProfileRepository;
import com.plus33.erp.finance.treasury.repository.TreasuryRiskPolicyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation of {@link TreasuryConfigurationRegistry}.
 * This implementation queries the database directly. In a production environment
 * with high-throughput, add a Spring Cache (@Cacheable) layer with Redis.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TreasuryConfigurationRegistryImpl implements TreasuryConfigurationRegistry {

    private final TreasuryAccountingProfileRepository profileRepository;
    private final TreasuryRiskPolicyRepository riskPolicyRepository;

    // Note: TreasuryLimitRepository is from the base module; injected for backward compatibility
    // When fully migrated to TreasuryRiskPolicy, TreasuryLimit can be deprecated.

    /**
     * Performs the resolveAccountingProfile operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param profileCode the profileCode input value
     * @return the TreasuryAccountingProfile result
     */
    @Override
    @Transactional
    public TreasuryAccountingProfile resolveAccountingProfile(Long companyId, String profileCode) {
        Optional<TreasuryAccountingProfile> profile = (profileCode != null)
                ? profileRepository.findByCompanyIdAndProfileCode(companyId, profileCode)
                : profileRepository.findFirstByCompanyIdOrderByProfileCodeAsc(companyId);

        return profile.orElseThrow(() -> new IllegalStateException(
                "No accounting profile found for company=" + companyId
                        + (profileCode != null ? " profileCode=" + profileCode : " (default)")));
    }

    /**
     * Retrieves active policies data from the database.
     *
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional
    public List<TreasuryRiskPolicy> getActivePolicies(Long companyId, String policyCategory,
                                                       LocalDate effectiveDate) {
        return riskPolicyRepository.findActivePolicies(companyId, policyCategory, effectiveDate);
    }

    /**
     * Retrieves best matching policy data from the database.
     *
     * @return Optional containing the entity if found, empty if not
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional
    public Optional<TreasuryRiskPolicy> findBestMatchingPolicy(Long companyId,
                                                                String policyCategory,
                                                                String policyName,
                                                                String currencyCode,
                                                                LocalDate effectiveDate) {
        List<TreasuryRiskPolicy> candidates = riskPolicyRepository.findMatchingPolicies(
                companyId, policyCategory, policyName, currencyCode, effectiveDate);
        // First result is most specific (currency-specific first due to ORDER BY)
        return candidates.stream().findFirst();
    }

    /**
     * Retrieves active limits data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves active limits data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public List<TreasuryLimit> getActiveLimits(Long companyId) {
        // Backward compat — returns empty until TreasuryLimit is fully migrated to TreasuryRiskPolicy
        log.debug("getActiveLimits delegated for company={} — migrate to TreasuryRiskPolicy", companyId);
        return Collections.emptyList();
    }

    /**
     * Performs the evictCache operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     */
    /**
     * Performs the evictCache operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     */
    @Override
    public void evictCache(Long companyId) {
        // No-op in this implementation — add @CacheEvict annotations when Redis is enabled
        log.info("Cache eviction requested for company={} (no-op in default impl)", companyId);
    }
}