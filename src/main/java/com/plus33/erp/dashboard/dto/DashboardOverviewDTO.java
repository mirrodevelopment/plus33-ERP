/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Dashboard Module
 * Package           : com.plus33.erp.dashboard.dto
 * File              : DashboardOverviewDTO.java
 * Purpose           : Data Transfer Object for request/response in Dashboard Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DashboardOverviewDTOController
 * Related Service   : DashboardOverviewDTOService, DashboardOverviewDTOServiceImpl
 * Related Repository: DashboardOverviewDTORepository
 * Related Entity    : DashboardOverviewDTO
 * Related DTO       : DashboardOverviewDTO
 * Related Mapper    : DashboardOverviewDTOMapper
 * Related DB Table  : dashboard_overview_d_t_os
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DashboardOverviewDTOController, DashboardOverviewDTOService, DashboardOverviewDTOServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Dashboard Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.dashboard.dto;

import java.util.*;

public class DashboardOverviewDTO {

    private Metadata metadata;
    private Map<String, Object> kpis = new HashMap<>();
    private Map<String, Object> salesOverview = new HashMap<>();
    private List<Map<String, Object>> regionalPerformance = new ArrayList<>();
    private List<Map<String, Object>> subRegionalPerformance = new ArrayList<>();
    private Map<String, Object> storeStatusOverview = new HashMap<>();
    private Map<String, Object> financialOverview = new HashMap<>();
    private Map<String, Object> inventoryOverview = new HashMap<>();
    private Map<String, Object> workforceOverview = new HashMap<>();
    private Map<String, Object> complianceOverview = new HashMap<>();
    private Map<String, Object> marketingOverview = new HashMap<>();
    private Map<String, Object> franchiseDevelopment = new HashMap<>();
    private List<Map<String, Object>> alerts = new ArrayList<>();
    private List<Map<String, Object>> recentActivities = new ArrayList<>();

    /**
     * Retrieves metadata data from the database.
     *
     * @return the Metadata result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Metadata getMetadata() { return metadata; }
    /**
     * Performs the setMetadata operation in this module.
     *
     * @param metadata the metadata input value
     */
    public void setMetadata(Metadata metadata) { this.metadata = metadata; }

    /**
     * Retrieves kpis data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Map<String, Object> getKpis() { return kpis; }
    /**
     * Performs the setKpis operation in this module.
     *
     * @param kpis the kpis input value
     */
    public void setKpis(Map<String, Object> kpis) { this.kpis = kpis; }

    /**
     * Retrieves sales overview data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Map<String, Object> getSalesOverview() { return salesOverview; }
    /**
     * Performs the setSalesOverview operation in this module.
     *
     * @param salesOverview the salesOverview input value
     */
    public void setSalesOverview(Map<String, Object> salesOverview) { this.salesOverview = salesOverview; }

    /**
     * Retrieves regional performance data from the database.
     *
     * @return List of matching records the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public List<Map<String, Object>> getRegionalPerformance() { return regionalPerformance; }
    /**
     * Performs the setRegionalPerformance operation in this module.
     *
     * @param regionalPerformance the regionalPerformance input value
     */
    public void setRegionalPerformance(List<Map<String, Object>> regionalPerformance) { this.regionalPerformance = regionalPerformance; }

    public List<Map<String, Object>> getSubRegionalPerformance() { return subRegionalPerformance; }
    public void setSubRegionalPerformance(List<Map<String, Object>> subRegionalPerformance) { this.subRegionalPerformance = subRegionalPerformance; }

    /**
     * Retrieves store status overview data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Map<String, Object> getStoreStatusOverview() { return storeStatusOverview; }
    /**
     * Performs the setStoreStatusOverview operation in this module.
     *
     * @param storeStatusOverview the storeStatusOverview input value
     */
    public void setStoreStatusOverview(Map<String, Object> storeStatusOverview) { this.storeStatusOverview = storeStatusOverview; }

    /**
     * Retrieves financial overview data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Map<String, Object> getFinancialOverview() { return financialOverview; }
    /**
     * Performs the setFinancialOverview operation in this module.
     *
     * @param financialOverview the financialOverview input value
     */
    public void setFinancialOverview(Map<String, Object> financialOverview) { this.financialOverview = financialOverview; }

    /**
     * Retrieves inventory overview data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Map<String, Object> getInventoryOverview() { return inventoryOverview; }
    /**
     * Performs the setInventoryOverview operation in this module.
     *
     * @param inventoryOverview the inventoryOverview input value
     */
    public void setInventoryOverview(Map<String, Object> inventoryOverview) { this.inventoryOverview = inventoryOverview; }

    /**
     * Retrieves workforce overview data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Map<String, Object> getWorkforceOverview() { return workforceOverview; }
    /**
     * Performs the setWorkforceOverview operation in this module.
     *
     * @param workforceOverview the workforceOverview input value
     */
    public void setWorkforceOverview(Map<String, Object> workforceOverview) { this.workforceOverview = workforceOverview; }

    /**
     * Retrieves compliance overview data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Map<String, Object> getComplianceOverview() { return complianceOverview; }
    /**
     * Performs the setComplianceOverview operation in this module.
     *
     * @param complianceOverview the complianceOverview input value
     */
    public void setComplianceOverview(Map<String, Object> complianceOverview) { this.complianceOverview = complianceOverview; }

    /**
     * Retrieves marketing overview data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Map<String, Object> getMarketingOverview() { return marketingOverview; }
    /**
     * Performs the setMarketingOverview operation in this module.
     *
     * @param marketingOverview the marketingOverview input value
     */
    public void setMarketingOverview(Map<String, Object> marketingOverview) { this.marketingOverview = marketingOverview; }

    /**
     * Retrieves franchise development data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Map<String, Object> getFranchiseDevelopment() { return franchiseDevelopment; }
    /**
     * Performs the setFranchiseDevelopment operation in this module.
     *
     * @param franchiseDevelopment the franchiseDevelopment input value
     */
    public void setFranchiseDevelopment(Map<String, Object> franchiseDevelopment) { this.franchiseDevelopment = franchiseDevelopment; }

    /**
     * Retrieves alerts data from the database.
     *
     * @return List of matching records the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public List<Map<String, Object>> getAlerts() { return alerts; }
    /**
     * Performs the setAlerts operation in this module.
     *
     * @param alerts the alerts input value
     */
    public void setAlerts(List<Map<String, Object>> alerts) { this.alerts = alerts; }

    /**
     * Retrieves recent activities data from the database.
     *
     * @return List of matching records the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public List<Map<String, Object>> getRecentActivities() { return recentActivities; }
    /**
     * Performs the setRecentActivities operation in this module.
     *
     * @param recentActivities the recentActivities input value
     */
    public void setRecentActivities(List<Map<String, Object>> recentActivities) { this.recentActivities = recentActivities; }

    public static class Metadata {
        private String generatedAt;
        private Long executionTimeMs;
        private Map<String, String> appliedFilters;
        private String userRole;
        private String cacheStatus;

        public String getGeneratedAt() { return generatedAt; }
        public void setGeneratedAt(String generatedAt) { this.generatedAt = generatedAt; }

        public Long getExecutionTimeMs() { return executionTimeMs; }
        public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }

        public Map<String, String> getAppliedFilters() { return appliedFilters; }
        public void setAppliedFilters(Map<String, String> appliedFilters) { this.appliedFilters = appliedFilters; }

        public String getUserRole() { return userRole; }
        public void setUserRole(String userRole) { this.userRole = userRole; }

        public String getCacheStatus() { return cacheStatus; }
        public void setCacheStatus(String cacheStatus) { this.cacheStatus = cacheStatus; }
    }
}
