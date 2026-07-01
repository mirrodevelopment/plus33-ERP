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

    @Transactional
    public int evictCompany(Long companyId) {
        int count = jdbc.update("DELETE FROM bi_dashboard_cache WHERE company_id=?", companyId);
        log.info("[CACHE] Evicted {} entries for companyId={}", count, companyId);
        return count;
    }

    @Transactional
    public int evictExpired() {
        int count = jdbc.update("DELETE FROM bi_dashboard_cache WHERE expires_at < CURRENT_TIMESTAMP");
        log.info("[CACHE] Removed {} expired cache entries.", count);
        return count;
    }
}
