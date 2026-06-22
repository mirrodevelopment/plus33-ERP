package com.plus33.erp.workforce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.organization.dto.RegionRequest;
import com.plus33.erp.organization.dto.StoreRequest;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Region;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.organization.repository.RegionRepository;
import com.plus33.erp.organization.repository.StoreRepository;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import com.plus33.erp.workforce.dto.EmployeeRequest;
import com.plus33.erp.workforce.repository.EmployeeRepository;
import com.plus33.erp.workforce.repository.UserRegionRepository;
import com.plus33.erp.workforce.repository.UserStoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRegionRepository userRegionRepository;

    @Autowired
    private UserStoreRepository userStoreRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void resetDatabaseState() {
        // Remove any entities committed by a previous non-@Transactional run.
        // Deletion order respects FK constraints (children before parents).
        storeRepository.findByCode("INACTIVE_STORE").ifPresent(storeRepository::delete);
        storeRepository.findByCode("STORE_2").ifPresent(storeRepository::delete);
        employeeRepository.findByEmployeeCode("EMP_001").ifPresent(employeeRepository::delete);
        userRepository.findByEmail("emp1@plus33.com").ifPresent(userRepository::delete);
        regionRepository.findByCode("REG_2").ifPresent(regionRepository::delete);
        companyRepository.findByCode("COMP_2").ifPresent(companyRepository::delete);
        // Flush queued deletes because IDENTITY inserts execute immediately.
        userRepository.flush();
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "EMPLOYEE_CREATE", "EMPLOYEE_VIEW", "EMPLOYEE_UPDATE", "EMPLOYEE_DELETE",
            "REGION_CREATE", "STORE_CREATE"
    })
    public void testEmployeeValidations() throws Exception {
        Company company = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseThrow(() -> new AssertionError("PLUS33_GLOBAL not found"));

        Region region = regionRepository.findByCode("UAE_REGION")
                .orElseThrow(() -> new AssertionError("UAE_REGION not found"));

        Store store = storeRepository.findByCode("DUBAI_MALL_STORE")
                .orElseThrow(() -> new AssertionError("DUBAI_MALL_STORE not found"));

        // 1. Create a dummy user
        User user1 = new User();
        user1.setEmail("emp1@plus33.com");
        user1.setPassword("password");
        user1.setFirstName("Emp");
        user1.setLastName("One");
        user1.setActive(true);
        user1 = userRepository.save(user1);

        // 2. Create valid employee
        EmployeeRequest validRequest = new EmployeeRequest(
                "EMP_001", "Emp", "One", "emp1@plus33.com", "12345",
                company.getId(), region.getId(), store.getId(), user1.getId(),
                "Software Engineer", "IT", "FULL_TIME", LocalDate.now(), true);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.employeeCode").value("EMP_001"));

        // 3. Test: Duplicate employee code
        EmployeeRequest dupCodeRequest = new EmployeeRequest(
                "EMP_001", "Emp", "Two", "emp2@plus33.com", "12345",
                company.getId(), region.getId(), store.getId(), null,
                "Software Engineer", "IT", "FULL_TIME", LocalDate.now(), true);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dupCodeRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Employee with code EMP_001 already exists in company PLUS33 Global Corp"));

        // 4. Test: Duplicate email
        EmployeeRequest dupEmailRequest = new EmployeeRequest(
                "EMP_002", "Emp", "Two", "emp1@plus33.com", "12345",
                company.getId(), region.getId(), store.getId(), null,
                "Software Engineer", "IT", "FULL_TIME", LocalDate.now(), true);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dupEmailRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Employee with email emp1@plus33.com already exists in company PLUS33 Global Corp"));

        // 5. Test: Duplicate user assignment
        EmployeeRequest dupUserRequest = new EmployeeRequest(
                "EMP_003", "Emp", "Three", "emp3@plus33.com", "12345",
                company.getId(), region.getId(), store.getId(), user1.getId(),
                "Software Engineer", "IT", "FULL_TIME", LocalDate.now(), true);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dupUserRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("User account is already assigned to another employee"));

        // 6. Test: Region outside company
        // Create region under non-existent or secondary company
        Company company2 = new Company();
        company2.setCode("COMP_2");
        company2.setName("Company 2");
        company2.setActive(true);
        company2 = companyRepository.save(company2);

        Region region2 = new Region();
        region2.setCode("REG_2");
        region2.setName("Region 2");
        region2.setCompany(company2);
        region2 = regionRepository.save(region2);

        EmployeeRequest regionErrRequest = new EmployeeRequest(
                "EMP_004", "Emp", "Four", "emp4@plus33.com", "12345",
                company.getId(), region2.getId(), null, null,
                "Software Engineer", "IT", "FULL_TIME", LocalDate.now(), true);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regionErrRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Cannot assign employee: Region does not belong to the selected company"));

        // 7. Test: Store outside region
        Store store2 = new Store();
        store2.setCode("STORE_2");
        store2.setName("Store 2");
        store2.setRegion(region2);
        store2.setActive(true);
        store2 = storeRepository.save(store2);

        EmployeeRequest storeErrRequest = new EmployeeRequest(
                "EMP_005", "Emp", "Five", "emp5@plus33.com", "12345",
                company.getId(), region.getId(), store2.getId(), null,
                "Software Engineer", "IT", "FULL_TIME", LocalDate.now(), true);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeErrRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Cannot assign employee: Store does not belong to the selected region"));

        // 8. Test: Inactive store assignment
        Store inactiveStore = new Store();
        inactiveStore.setCode("INACTIVE_STORE");
        inactiveStore.setName("Inactive Store");
        inactiveStore.setRegion(region);
        inactiveStore.setActive(false);
        inactiveStore = storeRepository.save(inactiveStore);

        EmployeeRequest inactiveStoreRequest = new EmployeeRequest(
                "EMP_006", "Emp", "Six", "emp6@plus33.com", "12345",
                company.getId(), region.getId(), inactiveStore.getId(), null,
                "Software Engineer", "IT", "FULL_TIME", LocalDate.now(), true);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inactiveStoreRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Cannot assign employee: Store is inactive"));

        // 9. Test: Employee deactivation removes user assignments
        var empOpt = employeeRepository.findByEmployeeCode("EMP_001");
        assertTrue(empOpt.isPresent());
        Long empId = empOpt.get().getId();

        mockMvc.perform(patch("/api/v1/employees/" + empId + "/deactivate"))
                .andExpect(status().isOk());

        // Assert user_regions and user_stores assignments are deleted
        assertFalse(userRegionRepository.existsByIdRegionId(region.getId()));
        assertFalse(userStoreRepository.existsByIdStoreId(store.getId()));
    }
}
