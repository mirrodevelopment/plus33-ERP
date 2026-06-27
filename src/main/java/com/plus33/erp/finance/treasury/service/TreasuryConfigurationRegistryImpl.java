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

    @Override
    @Transactional
    public List<TreasuryRiskPolicy> getActivePolicies(Long companyId, String policyCategory,
                                                       LocalDate effectiveDate) {
        return riskPolicyRepository.findActivePolicies(companyId, policyCategory, effectiveDate);
    }

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

    @Override
    public List<TreasuryLimit> getActiveLimits(Long companyId) {
        // Backward compat — returns empty until TreasuryLimit is fully migrated to TreasuryRiskPolicy
        log.debug("getActiveLimits delegated for company={} — migrate to TreasuryRiskPolicy", companyId);
        return Collections.emptyList();
    }

    @Override
    public void evictCache(Long companyId) {
        // No-op in this implementation — add @CacheEvict annotations when Redis is enabled
        log.info("Cache eviction requested for company={} (no-op in default impl)", companyId);
    }
}
