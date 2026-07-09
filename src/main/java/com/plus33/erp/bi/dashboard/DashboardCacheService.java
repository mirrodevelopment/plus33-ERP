/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.dashboard
 * File              : DashboardCacheService.java
 * Purpose           : Business logic service layer for Bi Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DashboardCacheController
 * Related Service   : DashboardCacheService
 * Related Repository: DashboardCacheRepository
 * Related Entity    : DashboardCache
 * Related DTO       : N/A
 * Related Mapper    : DashboardCacheMapper
 * Related DB Table  : dashboard_caches
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DashboardCacheController, DashboardCacheServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Bi Module. Implements DashboardCacheService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.bi.dashboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * DashboardCacheService: Company-isolated cache for dashboard widget responses.
 * Uses the bi_dashboard_cache table for persistence across restarts.
 */
@Service
public class DashboardCacheService {

    private static final Logger log = LoggerFactory.getLogger(DashboardCacheService.class);
    private final JdbcTemplate jdbc;

    public DashboardCacheService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Retrieves bi data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param cacheKey the cacheKey input value
     * @return Optional containing the entity if found, empty if not the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Optional<String> get(Long companyId, String cacheKey) {
        String sql = "SELECT cached_value FROM bi_dashboard_cache WHERE company_id=? AND cache_key=? AND expires_at > CURRENT_TIMESTAMP";
        try {
            String value = jdbc.queryForObject(sql, String.class, companyId, cacheKey);
            jdbc.update("UPDATE bi_dashboard_cache SET hit_count=hit_count+1, last_accessed_at=? WHERE company_id=? AND cache_key=?",
                    LocalDateTime.now(), companyId, cacheKey);
            return Optional.ofNullable(value);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Performs the put operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param cacheKey the cacheKey input value
     * @param value the value input value
     * @param ttlSeconds the ttlSeconds input value
     */
    @Transactional
    public void put(Long companyId, String cacheKey, String value, int ttlSeconds) {
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(ttlSeconds);
        jdbc.update("""
            INSERT INTO bi_dashboard_cache(company_id, cache_key, cached_value, ttl_seconds, expires_at, created_at)
            VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
            ON CONFLICT (company_id, cache_key) DO UPDATE
            SET cached_value=EXCLUDED.cached_value, expires_at=EXCLUDED.expires_at, hit_count=0, last_accessed_at=null
            """, companyId, cacheKey, value, ttlSeconds, expiresAt);
    }

    /**
     * Performs the evictCompany operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return the numeric result value
     */
    @Transactional
    public int evictCompany(Long companyId) {
        int count = jdbc.update("DELETE FROM bi_dashboard_cache WHERE company_id=?", companyId);
        log.info("[CACHE] Evicted {} entries for companyId={}", count, companyId);
        return count;
    }

    /**
     * Performs the evictExpired operation in this module.
     *
     * @return the numeric result value
     */
    @Transactional
    public int evictExpired() {
        int count = jdbc.update("DELETE FROM bi_dashboard_cache WHERE expires_at < CURRENT_TIMESTAMP");
        log.info("[CACHE] Removed {} expired cache entries.", count);
        return count;
    }
}