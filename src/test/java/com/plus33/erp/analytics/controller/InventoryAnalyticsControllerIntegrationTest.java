package com.plus33.erp.analytics.controller;

import com.plus33.erp.analytics.service.InventoryAnalyticsService;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.organization.repository.StoreRepository;
import com.plus33.erp.organization.repository.WarehouseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class InventoryAnalyticsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private InventoryAnalyticsService inventoryAnalyticsService;

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {"INVENTORY_ANALYTICS_VIEW", "INVENTORY_ANALYTICS_REFRESH"})
    public void testInventoryAnalyticsEndpoints() throws Exception {
        // 1. Fetch default global company, warehouse, and store
        Company company = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseThrow(() -> new AssertionError("PLUS33_GLOBAL not found"));

        Warehouse warehouse = warehouseRepository.findAll().stream()
                .filter(w -> w.getRegion().getCompany().getId().equals(company.getId()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No warehouse found for company"));

        Store store = storeRepository.findAll().stream()
                .filter(s -> s.getRegion().getCompany().getId().equals(company.getId()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No store found for company"));

        // 2. Perform materialized view refresh execution
        mockMvc.perform(post("/api/v1/analytics/inventory/refresh")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(containsString("refresh triggered")));

        // 3. Verify Refresh health endpoint
        mockMvc.perform(get("/api/v1/analytics/inventory/health")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.data[*].viewName", hasItem("mv_inventory_kpis")))
                .andExpect(jsonPath("$.data[*].refreshStatus", hasItem("SUCCESS")));

        // 4. Verify Empty dataset handling & Company isolation
        // Create a temporary isolated company to test zero results (empty metrics handling)
        Company isoCompany = new Company();
        isoCompany.setName("Isolated Test Company");
        isoCompany.setCode("ISO_COMP_" + System.nanoTime());
        isoCompany = companyRepository.save(isoCompany);

        // Fetch metrics for isolated company and verify it returns safe default values (no NPEs)
        mockMvc.perform(get("/api/v1/analytics/inventory/kpis")
                        .param("companyId", isoCompany.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.companyId").value(isoCompany.getId()))
                .andExpect(jsonPath("$.data.totalStockValue").value(0))
                .andExpect(jsonPath("$.data.totalUniqueProducts").value(0))
                .andExpect(jsonPath("$.data.outOfStockProducts").value(0));

        // 5. Verify KPI response validation for the main company
        mockMvc.perform(get("/api/v1/analytics/inventory/kpis")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.companyId").value(company.getId()))
                .andExpect(jsonPath("$.data.totalStockValue").exists())
                .andExpect(jsonPath("$.data.totalUniqueProducts").value(greaterThanOrEqualTo(0)));

        // 6. Verify Warehouse / Store filtering
        // Warehouse filtering
        mockMvc.perform(get("/api/v1/analytics/inventory/kpis")
                        .param("companyId", company.getId().toString())
                        .param("warehouseId", warehouse.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.warehouseId").value(warehouse.getId()))
                .andExpect(jsonPath("$.data.storeId").isEmpty());

        // Store filtering
        mockMvc.perform(get("/api/v1/analytics/inventory/kpis")
                        .param("companyId", company.getId().toString())
                        .param("storeId", store.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.storeId").value(store.getId()))
                .andExpect(jsonPath("$.data.warehouseId").isEmpty());

        // 7. Verify Dashboard endpoint aggregation (KPI, Aging, Replenishment, Traceability, Turnover)
        mockMvc.perform(get("/api/v1/analytics/inventory/dashboard")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.kpis").exists())
                .andExpect(jsonPath("$.data.agingExpiry").exists())
                .andExpect(jsonPath("$.data.replenishment").exists())
                .andExpect(jsonPath("$.data.traceability").exists())
                .andExpect(jsonPath("$.data.turnover").exists())
                .andExpect(jsonPath("$.data.kpis.companyId").value(company.getId()))
                .andExpect(jsonPath("$.data.agingExpiry.companyId").value(company.getId()))
                .andExpect(jsonPath("$.data.replenishment.companyId").value(company.getId()))
                .andExpect(jsonPath("$.data.traceability.companyId").value(company.getId()))
                .andExpect(jsonPath("$.data.turnover.companyId").value(company.getId()));

        // 8. Verify specific detail views
        // Aging-expiry
        mockMvc.perform(get("/api/v1/analytics/inventory/aging-expiry")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.companyId").value(company.getId()));

        // ABC/XYZ
        mockMvc.perform(get("/api/v1/analytics/inventory/abc-xyz")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());

        // Slow-dead
        mockMvc.perform(get("/api/v1/analytics/inventory/slow-dead")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());

        // Turnover
        mockMvc.perform(get("/api/v1/analytics/inventory/turnover")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.companyId").value(company.getId()));

        // Replenishment metrics
        mockMvc.perform(get("/api/v1/analytics/inventory/replenishment-metrics")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.companyId").value(company.getId()));

        // Traceability metrics
        mockMvc.perform(get("/api/v1/analytics/inventory/traceability-metrics")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.companyId").value(company.getId()));
    }
}
