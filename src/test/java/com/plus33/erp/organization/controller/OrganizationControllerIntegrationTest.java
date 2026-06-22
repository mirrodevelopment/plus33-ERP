package com.plus33.erp.organization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.organization.dto.*;
import com.plus33.erp.organization.entity.*;
import com.plus33.erp.organization.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrganizationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "COMPANY_VIEW", "COMPANY_UPDATE",
            "REGION_CREATE", "REGION_VIEW", "REGION_UPDATE", "REGION_DELETE",
            "WAREHOUSE_CREATE", "WAREHOUSE_VIEW", "WAREHOUSE_UPDATE", "WAREHOUSE_DELETE",
            "STORE_CREATE", "STORE_VIEW", "STORE_UPDATE", "STORE_DELETE"
    })
    public void testCreateStoreWithWarehouseFromAnotherRegion_Returns400() throws Exception {
        Company company = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseThrow(() -> new AssertionError("PLUS33_GLOBAL not found"));

        // 1. Create a second region in the company
        RegionRequest region2Request = new RegionRequest(
                "TEST_REGION_2_" + System.nanoTime(), "Test Region 2", "Secondary Region", company.getId());

        String region2Json = mockMvc.perform(post("/api/v1/regions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(region2Request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long region2Id = objectMapper.readTree(region2Json).path("data").path("id").asLong();

        // 2. Get the existing warehouse DUBAI_WAREHOUSE (which is in UAE_REGION / region 1)
        Warehouse warehouse = warehouseRepository.findByCode("DUBAI_WAREHOUSE")
                .orElseThrow(() -> new AssertionError("DUBAI_WAREHOUSE not found"));

        // 3. Create a store in TEST_REGION_2 referencing the warehouse in UAE_REGION
        StoreRequest storeRequest = new StoreRequest(
                "TEST_STORE_ERR_" + System.nanoTime(), "Test Store Error", "Address", "Phone", "Email", "Asia/Dubai",
                null, region2Id, warehouse.getId(), true);

        mockMvc.perform(post("/api/v1/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Cannot assign warehouse: warehouse region does not match store region"));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "COMPANY_VIEW", "COMPANY_UPDATE",
            "REGION_CREATE", "REGION_VIEW", "REGION_UPDATE", "REGION_DELETE",
            "WAREHOUSE_CREATE", "WAREHOUSE_VIEW", "WAREHOUSE_UPDATE", "WAREHOUSE_DELETE",
            "STORE_CREATE", "STORE_VIEW", "STORE_UPDATE", "STORE_DELETE"
    })
    public void testDeleteRegionWithActiveStores_Returns400() throws Exception {
        Region region = regionRepository.findByCode("UAE_REGION")
                .orElseThrow(() -> new AssertionError("UAE_REGION not found"));

        mockMvc.perform(delete("/api/v1/regions/" + region.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Cannot delete region")));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "COMPANY_VIEW", "COMPANY_UPDATE",
            "REGION_CREATE", "REGION_VIEW", "REGION_UPDATE", "REGION_DELETE",
            "WAREHOUSE_CREATE", "WAREHOUSE_VIEW", "WAREHOUSE_UPDATE", "WAREHOUSE_DELETE",
            "STORE_CREATE", "STORE_VIEW", "STORE_UPDATE", "STORE_DELETE"
    })
    public void testAssignInactiveWarehouseToStore_Returns400() throws Exception {
        Region region = regionRepository.findByCode("UAE_REGION")
                .orElseThrow(() -> new AssertionError("UAE_REGION not found"));

        // 1. Create an inactive warehouse in UAE_REGION
        WarehouseRequest inactiveWhRequest = new WarehouseRequest(
                "INACTIVE_WH_" + System.nanoTime(), "Inactive Warehouse", "Address", "Phone", "Email", "Asia/Dubai",
                null, region.getId(), false);

        String inactiveWhJson = mockMvc.perform(post("/api/v1/warehouses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inactiveWhRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long inactiveWhId = objectMapper.readTree(inactiveWhJson).path("data").path("id").asLong();

        // 2. Create a store in UAE_REGION referencing the inactive warehouse
        StoreRequest storeRequest = new StoreRequest(
                "TEST_STORE_ERR2_" + System.nanoTime(), "Test Store Error 2", "Address", "Phone", "Email", "Asia/Dubai",
                null, region.getId(), inactiveWhId, true);

        mockMvc.perform(post("/api/v1/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Cannot assign warehouse: referenced warehouse is inactive"));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "COMPANY_VIEW", "COMPANY_UPDATE",
            "REGION_CREATE", "REGION_VIEW", "REGION_UPDATE", "REGION_DELETE",
            "WAREHOUSE_CREATE", "WAREHOUSE_VIEW", "WAREHOUSE_UPDATE", "WAREHOUSE_DELETE",
            "STORE_CREATE", "STORE_VIEW", "STORE_UPDATE", "STORE_DELETE"
    })
    public void testDeactivateWarehouseUsedByActiveStores_Returns400() throws Exception {
        Warehouse warehouse = warehouseRepository.findByCode("DUBAI_WAREHOUSE")
                .orElseThrow(() -> new AssertionError("DUBAI_WAREHOUSE not found"));

        mockMvc.perform(patch("/api/v1/warehouses/" + warehouse.getId() + "/deactivate"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Cannot deactivate warehouse: active stores depend on it as their default warehouse"));
    }
}
