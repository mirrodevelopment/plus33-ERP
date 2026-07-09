package com.plus33.erp.workforce.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@org.springframework.test.context.TestPropertySource(properties = {
    "jwt.secret=plus33-super-secret-key-for-development-minimum-32-characters"
})
public class VerifyCountryPoliciesIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "emp3_st_fr_reg_3_01@plus33.com", authorities = {"SENIOR_EMPLOYEE"})
    public void testFranceEmployeePolicies() throws Exception {
        // Verify /me returns France
        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.country", is("France")));

        // Verify /types returns France-resolved policy details
        mockMvc.perform(get("/api/v1/leaves/types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[?(@.code=='ANNUAL')].annualLimit", contains(25.0)))
                .andExpect(jsonPath("$.data[?(@.code=='CASUAL')].annualLimit", contains(3.0)));
    }

    @Test
    @WithMockUser(username = "emp3_st_in_reg_3_01@plus33.com", authorities = {"SENIOR_EMPLOYEE"})
    public void testIndiaEmployeePolicies() throws Exception {
        // Verify /me returns India
        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.country", is("India")));

        // Verify /types returns India-resolved policy details (Earned: 18, Casual: 12)
        mockMvc.perform(get("/api/v1/leaves/types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[?(@.code=='ANNUAL')].annualLimit", contains(18.0)))
                .andExpect(jsonPath("$.data[?(@.code=='CASUAL')].annualLimit", contains(12.0)));
    }

    @Test
    @WithMockUser(username = "emp3_st_ae_reg_1_01@plus33.com", authorities = {"SENIOR_EMPLOYEE"})
    public void testUaeEmployeePolicies() throws Exception {
        // Verify /me returns UAE
        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.country", is("UAE")));

        // Verify /types returns UAE-resolved policy details (Annual: 30, Casual: 3, Paternity: 5)
        mockMvc.perform(get("/api/v1/leaves/types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[?(@.code=='ANNUAL')].annualLimit", contains(30.0)))
                .andExpect(jsonPath("$.data[?(@.code=='CASUAL')].annualLimit", contains(3.0)))
                .andExpect(jsonPath("$.data[?(@.code=='PATERNITY')].annualLimit", contains(5.0)));
    }
}
