package com.plus33.erp.bi.scheduler;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * BiConfigurationRegistry: Runtime access to the bi_configuration table.
 * Provides typed getters for common operational parameters.
 */
@Component
public class BiConfigurationRegistry {

    private final JdbcTemplate jdbc;

    public BiConfigurationRegistry(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public String getString(String key, String defaultValue) {
        try {
            String value = jdbc.queryForObject("SELECT config_value FROM bi_configuration WHERE config_key=?", String.class, key);
            return value != null ? value : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(getString(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public Map<String, String> getAllByGroup(String group) {
        Map<String, String> result = new HashMap<>();
        jdbc.query("SELECT config_key, config_value FROM bi_configuration WHERE config_group=?",
            new Object[]{group},
            rs -> { result.put(rs.getString("config_key"), rs.getString("config_value")); });
        return result;
    }

    public int getCdcBatchSize() { return getInt("CDC_BATCH_SIZE", 5000); }
    public int getMaxEtlThreads() { return getInt("MAX_ETL_THREADS", 4); }
    public int getMaxRetryCount() { return getInt("MAX_RETRY_COUNT", 3); }
    public String getDefaultRefreshMode() { return getString("DEFAULT_REFRESH_MODE", "INCREMENTAL"); }
    public String getDefaultForecastModel() { return getString("DEFAULT_FORECAST_MODEL", "LINEAR"); }
    public int getStagingRetentionDays() { return getInt("STAGING_RETENTION_DAYS", 30); }
}
