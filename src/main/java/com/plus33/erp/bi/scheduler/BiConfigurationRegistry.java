/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.scheduler
 * File              : BiConfigurationRegistry.java
 * Purpose           : Component of Bi Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiConfigurationRegistryController
 * Related Service   : BiConfigurationRegistryService, BiConfigurationRegistryServiceImpl
 * Related Repository: BiConfigurationRegistryRepository
 * Related Entity    : BiConfigurationRegistry
 * Related DTO       : N/A
 * Related Mapper    : BiConfigurationRegistryMapper
 * Related DB Table  : bi_configuration_registrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Bi Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Bi Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
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

    /**
     * Retrieves string data from the database.
     *
     * @param key the key input value
     * @param defaultValue the defaultValue input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getString(String key, String defaultValue) {
        try {
            String value = jdbc.queryForObject("SELECT config_value FROM bi_configuration WHERE config_key=?", String.class, key);
            return value != null ? value : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Retrieves int data from the database.
     *
     * @param key the key input value
     * @param defaultValue the defaultValue input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(getString(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Retrieves a paginated list of all by group records.
     *
     * @param group the group input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Map<String, String> getAllByGroup(String group) {
        Map<String, String> result = new HashMap<>();
        jdbc.query("SELECT config_key, config_value FROM bi_configuration WHERE config_group=?",
            new Object[]{group},
            rs -> { result.put(rs.getString("config_key"), rs.getString("config_value")); });
        return result;
    }

    /**
     * Retrieves cdc batch size data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getCdcBatchSize() { return getInt("CDC_BATCH_SIZE", 5000); }
    /**
     * Retrieves max etl threads data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getMaxEtlThreads() { return getInt("MAX_ETL_THREADS", 4); }
    /**
     * Retrieves max retry count data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getMaxRetryCount() { return getInt("MAX_RETRY_COUNT", 3); }
    /**
     * Retrieves default refresh mode data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDefaultRefreshMode() { return getString("DEFAULT_REFRESH_MODE", "INCREMENTAL"); }
    /**
     * Retrieves default forecast model data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDefaultForecastModel() { return getString("DEFAULT_FORECAST_MODEL", "LINEAR"); }
    /**
     * Retrieves staging retention days data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getStagingRetentionDays() { return getInt("STAGING_RETENTION_DAYS", 30); }
}
