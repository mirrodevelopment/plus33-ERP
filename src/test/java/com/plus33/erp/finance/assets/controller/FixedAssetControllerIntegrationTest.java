package com.plus33.erp.finance.assets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.finance.assets.dto.*;
import com.plus33.erp.finance.assets.entity.*;
import com.plus33.erp.finance.assets.repository.*;
import com.plus33.erp.finance.entity.*;
import com.plus33.erp.finance.repository.*;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.organization.repository.StoreRepository;
import com.plus33.erp.organization.repository.WarehouseRepository;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import com.plus33.erp.workforce.entity.Employee;
import com.plus33.erp.workforce.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FixedAssetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private JournalEntryLineRepository journalEntryLineRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AssetCategoryRepository assetCategoryRepository;

    @Autowired
    private FixedAssetRepository fixedAssetRepository;

    @Autowired
    private FixedAssetDepreciationLogRepository fixedAssetDepreciationLogRepository;

    @Autowired
    private FixedAssetTransferRepository fixedAssetTransferRepository;

    @Autowired
    private FixedAssetMaintenanceRepository fixedAssetMaintenanceRepository;

    @Autowired
    private FixedAssetAssignmentRepository fixedAssetAssignmentRepository;

    @Autowired
    private FixedAssetAuditRepository fixedAssetAuditRepository;

    @Autowired
    private FixedAssetAuditItemRepository fixedAssetAuditItemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Company company;
    private User adminUser;
    private Warehouse warehouse;
    private Store store;
    private Employee employee;

    private Account assetGLAccount;
    private Account accumDeprGLAccount;
    private Account deprExpenseGLAccount;
    private Account gainLossGLAccount;
    
    private AssetCategory category;

    @BeforeEach
    public void setUp() {
        cleanup();

        // 1. Load Company
        company = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseThrow(() -> new AssertionError("PLUS33_GLOBAL company not found"));

        // 2. Load Admin User
        adminUser = userRepository.findByEmail("admin@plus33.com")
                .orElseThrow(() -> new AssertionError("admin@plus33.com user not found"));

        // 3. Load standard seeded warehouse & store
        warehouse = warehouseRepository.findByCode("DUBAI_WAREHOUSE")
                .orElseThrow(() -> new AssertionError("DUBAI_WAREHOUSE not found"));

        store = storeRepository.findByCode("DUBAI_MALL_STORE")
                .orElseThrow(() -> new AssertionError("DUBAI_MALL_STORE not found"));

        // 4. Create test employee using setter constructor
        employee = new Employee();
        employee.setCompany(company);
        employee.setEmployeeCode("EMP-FA-TEST-" + System.nanoTime());
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe.test@plus33.com");
        employee.setDesignation("Engineer");
        employee.setEmploymentType("FULL_TIME");
        employee.setHireDate(LocalDate.now());
        employee.setStatus("ACTIVE");
        employee.setActive(true);
        employee = employeeRepository.save(employee);

        // 5. Create custom Accounts for Fixed Assets
        assetGLAccount = Account.builder()
                .company(company)
                .accountCode("1500")
                .accountName("Equipment Asset")
                .accountType("ASSET")
                .active(true)
                .build();
        assetGLAccount = accountRepository.save(assetGLAccount);

        accumDeprGLAccount = Account.builder()
                .company(company)
                .accountCode("1510")
                .accountName("Accumulated Depreciation - Equipment")
                .accountType("ASSET")
                .active(true)
                .build();
        accumDeprGLAccount = accountRepository.save(accumDeprGLAccount);

        deprExpenseGLAccount = Account.builder()
                .company(company)
                .accountCode("5500")
                .accountName("Depreciation Expense")
                .accountType("EXPENSE")
                .active(true)
                .build();
        deprExpenseGLAccount = accountRepository.save(deprExpenseGLAccount);

        gainLossGLAccount = Account.builder()
                .company(company)
                .accountCode("5600")
                .accountName("Gain/Loss on Disposal")
                .accountType("EXPENSE")
                .active(true)
                .build();
        gainLossGLAccount = accountRepository.save(gainLossGLAccount);

        // 6. Create Asset Category
        category = AssetCategory.builder()
                .company(company)
                .code("EQP")
                .name("Equipment")
                .depreciationMethod(DepreciationMethod.STRAIGHT_LINE)
                .depreciationRate(BigDecimal.valueOf(20))
                .usefulLifeYears(5)
                .assetAccount(assetGLAccount)
                .accumulatedDepreciationAccount(accumDeprGLAccount)
                .depreciationExpenseAccount(deprExpenseGLAccount)
                .gainLossAccount(gainLossGLAccount)
                .build();
        category = assetCategoryRepository.save(category);
    }

    @AfterEach
    public void tearDown() {
        cleanup();
    }

    private void cleanup() {
        fixedAssetAuditItemRepository.deleteAll();
        fixedAssetAuditRepository.deleteAll();
        fixedAssetAssignmentRepository.deleteAll();
        fixedAssetMaintenanceRepository.deleteAll();
        fixedAssetTransferRepository.deleteAll();
        fixedAssetDepreciationLogRepository.deleteAll();
        fixedAssetRepository.deleteAll();
        
        if (category != null) {
            // Remove any budget_dimension_sets that reference this category (committed via MockMvc CapEx budget calls)
            // before deleting the category itself to avoid fk_dim_sets_ac violation.
            jdbcTemplate.update("DELETE FROM budget_dimension_sets WHERE asset_category_id = ?", category.getId());
            assetCategoryRepository.delete(category);
            category = null;
        }

        // Clean up GL entries created by tests
        journalEntryLineRepository.deleteAll();
        journalEntryRepository.deleteAll();

        // Clean up custom accounts
        if (assetGLAccount != null) {
            accountRepository.delete(assetGLAccount);
            assetGLAccount = null;
        }
        if (accumDeprGLAccount != null) {
            accountRepository.delete(accumDeprGLAccount);
            accumDeprGLAccount = null;
        }
        if (deprExpenseGLAccount != null) {
            accountRepository.delete(deprExpenseGLAccount);
            deprExpenseGLAccount = null;
        }
        if (gainLossGLAccount != null) {
            accountRepository.delete(gainLossGLAccount);
            gainLossGLAccount = null;
        }

        // Clean up custom test employee only (do not delete warehouse/store since they are seeded)
        if (employee != null) {
            employeeRepository.delete(employee);
            employee = null;
        }
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "FIXED_ASSET_VIEW", "FIXED_ASSET_CREATE", "FIXED_ASSET_ACQUIRE",
            "FIXED_ASSET_DEPRECIATE", "FIXED_ASSET_TRANSFER", "FIXED_ASSET_DISPOSE",
            "FIXED_ASSET_MAINTAIN", "FIXED_ASSET_ASSIGN", "FIXED_ASSET_AUDIT"
    })
    public void testFullFixedAssetLifecycleFlow() throws Exception {
        LocalDate acquisitionDate = LocalDate.now();

        // ─────────────────────────────────────────────────────────────────────
        // SCENARIO 1: Asset Number Generation (Register Draft Asset)
        // ─────────────────────────────────────────────────────────────────────
        FixedAssetRequest registerReq = new FixedAssetRequest(
                category.getId(),
                null,
                "Enterprise Test Server",
                "High performance database server",
                acquisitionDate,
                BigDecimal.valueOf(12000.00),
                BigDecimal.valueOf(2000.00),
                5,
                BigDecimal.valueOf(20.00),
                "STRAIGHT_LINE",
                warehouse.getId(),
                null,
                null,
                "http://storage/invoice.pdf",
                "http://storage/warranty.pdf",
                "http://storage/insurance.pdf",
                "http://storage/photo.jpg",
                "http://storage/manual.pdf",
                acquisitionDate,
                acquisitionDate.plusMonths(3),
                "Dell Technologies",
                acquisitionDate.plusMonths(2),
                acquisitionDate.plusMonths(2),
                "POL-123456",
                "AXA Insurance",
                BigDecimal.valueOf(500.00),
                acquisitionDate.plusMonths(2),
                BigDecimal.valueOf(10000.00)
        );

        String responseJson = mockMvc.perform(post("/api/v1/fixed-assets")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.assetCode").value(org.hamcrest.Matchers.matchesPattern("^AST-\\d{4}-\\d{6}$")))
                .andExpect(jsonPath("$.data.status").value("DRAFT"))
                .andExpect(jsonPath("$.data.currentBookValue").value(12000.00))
                .andExpect(jsonPath("$.data.qrCodeString").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        FixedAssetResponse registeredAsset = objectMapper.readValue(
                objectMapper.readTree(responseJson).get("data").toString(),
                FixedAssetResponse.class
        );

        // ─────────────────────────────────────────────────────────────────────
        // SCENARIO 2 & 3: Component Tree Depreciation & Dry-Run vs Live
        // ─────────────────────────────────────────────────────────────────────
        // 1. Capitalize the parent asset to transition status to ACTIVE (needed to add components and test depreciation)
        mockMvc.perform(post("/api/v1/fixed-assets/" + registeredAsset.id() + "/acquire")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));

        // 2. Register a child component (e.g. Server Power Supply)
        FixedAssetRequest componentReq = new FixedAssetRequest(
                category.getId(),
                registeredAsset.id(),
                "Power Supply Component",
                "Redundant backup power supply",
                acquisitionDate,
                BigDecimal.valueOf(1500.00),
                BigDecimal.valueOf(100.00),
                2,
                BigDecimal.valueOf(50.00),
                "STRAIGHT_LINE",
                warehouse.getId(),
                null,
                null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null
        );

        mockMvc.perform(post("/api/v1/fixed-assets")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(componentReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.parentAssetId").value(registeredAsset.id()));

        // 3. Capitalize the child component asset
        List<FixedAsset> components = fixedAssetRepository.findAllByParentAssetId(registeredAsset.id());
        assertEquals(1, components.size());
        FixedAsset childComponent = components.get(0);
        
        mockMvc.perform(post("/api/v1/fixed-assets/" + childComponent.getId() + "/acquire")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));

        // Trigger a Depreciation DRY RUN
        DepreciationRunRequest deprDryRunReq = new DepreciationRunRequest(acquisitionDate.plusMonths(1), true);
        mockMvc.perform(post("/api/v1/fixed-assets/depreciate")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deprDryRunReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.dryRun").value(true))
                .andExpect(jsonPath("$.data.assetsProcessedCount").value(2)) // Both parent & component
                .andExpect(jsonPath("$.data.projectedJournalEntries").isNotEmpty());

        // Verify database and asset book values are NOT changed after dry-run
        FixedAsset parentAssetDB = fixedAssetRepository.findById(registeredAsset.id()).orElseThrow();
        assertEquals(BigDecimal.valueOf(12000.00).setScale(2), parentAssetDB.getCurrentBookValue());

        // Trigger a LIVE Depreciation Run
        DepreciationRunRequest deprLiveRunReq = new DepreciationRunRequest(acquisitionDate.plusMonths(1), false);
        mockMvc.perform(post("/api/v1/fixed-assets/depreciate")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deprLiveRunReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.dryRun").value(false))
                .andExpect(jsonPath("$.data.assetsProcessedCount").value(2));

        // Verify database IS updated after live run
        parentAssetDB = fixedAssetRepository.findById(registeredAsset.id()).orElseThrow();
        // Straight line: (12000 - 2000) / (5 * 12) = 10000 / 60 = 166.67
        BigDecimal expectedBookValue = BigDecimal.valueOf(12000.00).subtract(BigDecimal.valueOf(166.67));
        assertEquals(expectedBookValue.setScale(2), parentAssetDB.getCurrentBookValue().setScale(2));

        // ─────────────────────────────────────────────────────────────────────
        // SCENARIO 4: Location & Employee Assignment History
        // ─────────────────────────────────────────────────────────────────────
        // Assign to employee
        AssetAssignmentRequest assignReq1 = new AssetAssignmentRequest(employee.getId(), "Engineering", null, null);
        mockMvc.perform(post("/api/v1/fixed-assets/" + registeredAsset.id() + "/assign")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignReq1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.assignedEmployeeId").value(employee.getId()))
                .andExpect(jsonPath("$.data.assignedDepartment").value("Engineering"));

        // Assign to store (physical transfer)
        AssetAssignmentRequest assignReq2 = new AssetAssignmentRequest(null, null, null, store.getId());
        mockMvc.perform(post("/api/v1/fixed-assets/" + registeredAsset.id() + "/assign")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignReq2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.assignedStoreId").value(store.getId()));

        // Verify history has 2 entries (first released, second active)
        List<FixedAssetAssignment> assignments = fixedAssetAssignmentRepository.findAllByFixedAssetId(registeredAsset.id());
        assertEquals(2, assignments.size());
        assertNotNull(assignments.get(0).getReleasedAt());
        assertNull(assignments.get(1).getReleasedAt());

        // ─────────────────────────────────────────────────────────────────────
        // SCENARIO 5: Physical Audits & Status Transitions
        // ─────────────────────────────────────────────────────────────────────
        // Create an audit checklist where the power supply component is MISSING
        List<AssetAuditItemRequest> auditItems = new ArrayList<>();
        auditItems.add(new AssetAuditItemRequest(childComponent.getId(), "MISSING", "Not found in rack 4B", null));
        auditItems.add(new AssetAuditItemRequest(registeredAsset.id(), "FOUND_OK", "Running fine", "http://storage/audit-photo.jpg"));

        AssetAuditRequest auditReq = new AssetAuditRequest(
                acquisitionDate,
                "Auditor David",
                warehouse.getId(),
                null,
                auditItems
        );

        mockMvc.perform(post("/api/v1/fixed-assets/audits")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auditReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.auditorName").value("Auditor David"));

        // Verify child component status transitioned to LOST
        FixedAsset childDB = fixedAssetRepository.findById(childComponent.getId()).orElseThrow();
        assertEquals(FixedAssetStatus.LOST, childDB.getStatus());

        // ─────────────────────────────────────────────────────────────────────
        // SCENARIO 6: Capitalized Maintenance
        // ─────────────────────────────────────────────────────────────────────
        AssetMaintenanceRequest maintReq = new AssetMaintenanceRequest(
                acquisitionDate.plusMonths(2),
                "RAM Upgrade & Disk Replacement",
                BigDecimal.valueOf(2500.00),
                true, // Capitalize!
                "Technical Team"
        );

        mockMvc.perform(post("/api/v1/fixed-assets/" + registeredAsset.id() + "/maintain")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(maintReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.capitalize").value(true))
                .andExpect(jsonPath("$.data.journalEntryId").isNotEmpty());

        // Verify book value and cost are adjusted upward in DB
        parentAssetDB = fixedAssetRepository.findById(registeredAsset.id()).orElseThrow();
        // originalCost = 12000 + 2500 = 14500
        assertEquals(BigDecimal.valueOf(14500.00).setScale(2), parentAssetDB.getOriginalCost().setScale(2));

        // ─────────────────────────────────────────────────────────────────────
        // SCENARIO 7: Asset Disposal (Gain/Loss)
        // ─────────────────────────────────────────────────────────────────────
        // Dispose of the server for a gain (proceeds = 15000)
        AssetDisposalRequest disposalReq = new AssetDisposalRequest(
                acquisitionDate.plusMonths(3),
                "DISPOSED",
                BigDecimal.valueOf(15000.00),
                "Sold to partner company"
        );

        mockMvc.perform(post("/api/v1/fixed-assets/" + registeredAsset.id() + "/dispose")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(disposalReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("DISPOSED"))
                .andExpect(jsonPath("$.data.proceeds").value(15000.00))
                // net book value before disposal was approximately: 12000 (acq) - 166.67 (depr) + 2500 (maint) = 14333.33
                // gain = 15000 - 14333.33 = 666.67
                .andExpect(jsonPath("$.data.gainLossAmount").value(666.67))
                .andExpect(jsonPath("$.data.journalEntryId").isNotEmpty());

        // Verify status in DB is DISPOSED
        parentAssetDB = fixedAssetRepository.findById(registeredAsset.id()).orElseThrow();
        assertEquals(FixedAssetStatus.DISPOSED, parentAssetDB.getStatus());
        assertEquals(BigDecimal.ZERO.setScale(2), parentAssetDB.getCurrentBookValue().setScale(2));

        // ─────────────────────────────────────────────────────────────────────
        // SCENARIOS 8 & 9: Dashboard & KPIs / Warranty & Insurance Tracking
        // ─────────────────────────────────────────────────────────────────────
        mockMvc.perform(get("/api/v1/fixed-assets/dashboard")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalAssetValue").isNotEmpty())
                .andExpect(jsonPath("$.data.netBookValue").isNotEmpty())
                .andExpect(jsonPath("$.data.insuranceExpiringCount").value(1))
                .andExpect(jsonPath("$.data.warrantyExpiringCount").value(1));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "FIXED_ASSET_VIEW", "FIXED_ASSET_CREATE", "FIXED_ASSET_ACQUIRE"
    })
    public void testCapitalizationThresholdRules() throws Exception {
        LocalDate acquisitionDate = LocalDate.now();

        // Configure a threshold of 1000.00 on the category
        category.setCapitalizationThreshold(BigDecimal.valueOf(1000.00));
        assetCategoryRepository.save(category);

        // 1. Asset BELOW threshold: $500.00
        FixedAssetRequest belowReq = new FixedAssetRequest(
                category.getId(), null, "Cheap Tool", "Under threshold",
                acquisitionDate, BigDecimal.valueOf(500.00), BigDecimal.ZERO, 5,
                BigDecimal.valueOf(20.00), "STRAIGHT_LINE", warehouse.getId(), null, null
        );

        String belowJson = mockMvc.perform(post("/api/v1/fixed-assets")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(belowReq)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long belowId = objectMapper.readTree(belowJson).get("data").get("id").asLong();

        // Acquire it
        mockMvc.perform(post("/api/v1/fixed-assets/" + belowId + "/acquire")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("EXPENSED"));

        // 2. Asset ABOVE threshold: $1500.00
        FixedAssetRequest aboveReq = new FixedAssetRequest(
                category.getId(), null, "Expensive Tool", "Above threshold",
                acquisitionDate, BigDecimal.valueOf(1500.00), BigDecimal.ZERO, 5,
                BigDecimal.valueOf(20.00), "STRAIGHT_LINE", warehouse.getId(), null, null
        );

        String aboveJson = mockMvc.perform(post("/api/v1/fixed-assets")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aboveReq)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long aboveId = objectMapper.readTree(aboveJson).get("data").get("id").asLong();

        // Acquire it
        mockMvc.perform(post("/api/v1/fixed-assets/" + aboveId + "/acquire")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "FIXED_ASSET_VIEW", "FIXED_ASSET_CREATE", "FIXED_ASSET_ACQUIRE"
    })
    public void testCapitalWorkInProgress() throws Exception {
        LocalDate date = LocalDate.now();

        // Register asset as CWIP (isCwip = true)
        FixedAssetRequest cwipReq = new FixedAssetRequest(
                category.getId(), null, "Factory Construction", "Under construction",
                date, BigDecimal.valueOf(50000.00), BigDecimal.ZERO, 10,
                BigDecimal.valueOf(10.00), "STRAIGHT_LINE", warehouse.getId(), null, null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                true, null, BigDecimal.valueOf(100000.00), null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null
        );

        String responseJson = mockMvc.perform(post("/api/v1/fixed-assets")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cwipReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("UNDER_CONSTRUCTION"))
                .andReturn().getResponse().getContentAsString();
        Long assetId = objectMapper.readTree(responseJson).get("data").get("id").asLong();

        // Capitalize CWIP
        AssetCapitalizeCwipRequest capReq = new AssetCapitalizeCwipRequest(
                date, BigDecimal.valueOf(55000.00), category.getId(), 10, BigDecimal.ZERO, "STRAIGHT_LINE"
        );

        mockMvc.perform(post("/api/v1/fixed-assets/" + assetId + "/capitalize-cwip")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(capReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ACTIVE"))
                .andExpect(jsonPath("$.data.acquisitionCost").value(55000.00));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "FIXED_ASSET_VIEW", "FIXED_ASSET_CREATE", "FIXED_ASSET_ACQUIRE", "FIXED_ASSET_SPLIT_MERGE"
    })
    public void testAssetSplitAndMerge() throws Exception {
        LocalDate date = LocalDate.now();

        // Register and capitalize parent asset ($10,000)
        FixedAssetRequest mainReq = new FixedAssetRequest(
                category.getId(), null, "Main Server Rack", "Rack with servers",
                date, BigDecimal.valueOf(10000.00), BigDecimal.ZERO, 5,
                BigDecimal.valueOf(20.00), "STRAIGHT_LINE", warehouse.getId(), null, null
        );

        String mainJson = mockMvc.perform(post("/api/v1/fixed-assets")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mainReq)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long mainId = objectMapper.readTree(mainJson).get("data").get("id").asLong();

        mockMvc.perform(post("/api/v1/fixed-assets/" + mainId + "/acquire")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk());

        // 1. SPLIT asset
        List<AssetSplitRequest.SplitTarget> targets = List.of(
                new AssetSplitRequest.SplitTarget("Server Node A", "First component", BigDecimal.valueOf(0.60)),
                new AssetSplitRequest.SplitTarget("Server Node B", "Second component", BigDecimal.valueOf(0.40))
        );
        AssetSplitRequest splitReq = new AssetSplitRequest(targets);

        String splitResult = mockMvc.perform(post("/api/v1/fixed-assets/" + mainId + "/split")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(splitReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andReturn().getResponse().getContentAsString();
        
        long childId1 = objectMapper.readTree(splitResult).get("data").get(0).get("id").asLong();
        long childId2 = objectMapper.readTree(splitResult).get("data").get(1).get("id").asLong();

        // 2. MERGE assets back
        AssetMergeRequest mergeReq = new AssetMergeRequest(
                List.of(childId1, childId2), "Merged Server Cluster", "Cluster of nodes"
        );

        mockMvc.perform(post("/api/v1/fixed-assets/merge")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mergeReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Merged Server Cluster"))
                .andExpect(jsonPath("$.data.acquisitionCost").value(10000.00));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "FIXED_ASSET_VIEW", "FIXED_ASSET_CREATE", "FIXED_ASSET_ACQUIRE", "FIXED_ASSET_RESERVE"
    })
    public void testSharedAssetReservations() throws Exception {
        LocalDate date = LocalDate.now();

        // Register and capitalize asset
        FixedAssetRequest assetReq = new FixedAssetRequest(
                category.getId(), null, "Shared Projector", "Conference room projector",
                date, BigDecimal.valueOf(2000.00), BigDecimal.ZERO, 5,
                BigDecimal.valueOf(20.00), "STRAIGHT_LINE", warehouse.getId(), null, null
        );

        String assetJson = mockMvc.perform(post("/api/v1/fixed-assets")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assetReq)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long assetId = objectMapper.readTree(assetJson).get("data").get("id").asLong();

        mockMvc.perform(post("/api/v1/fixed-assets/" + assetId + "/acquire")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk());

        // Create Reservation
        AssetReservationRequest resReq = new AssetReservationRequest(
                assetId, employee.getEmployeeCode(), "Engineering", date, date.plusDays(3), "Product demo"
        );

        mockMvc.perform(post("/api/v1/fixed-assets/reservations")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("REQUESTED"));

        // Retrieve reservations
        mockMvc.perform(get("/api/v1/fixed-assets/" + assetId + "/reservations")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "FIXED_ASSET_VIEW", "FIXED_ASSET_CREATE", "FIXED_ASSET_ACQUIRE"
    })
    public void testAssetUtilizationAndTco() throws Exception {
        LocalDate date = LocalDate.now();

        // Register and capitalize asset
        FixedAssetRequest assetReq = new FixedAssetRequest(
                category.getId(), null, "Fleet Vehicle", "Delivery van",
                date, BigDecimal.valueOf(30000.00), BigDecimal.ZERO, 5,
                BigDecimal.valueOf(20.00), "STRAIGHT_LINE", warehouse.getId(), null, null
        );

        String assetJson = mockMvc.perform(post("/api/v1/fixed-assets")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assetReq)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long assetId = objectMapper.readTree(assetJson).get("data").get("id").asLong();

        mockMvc.perform(post("/api/v1/fixed-assets/" + assetId + "/acquire")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk());

        // Log utilization
        AssetUtilizationRequest utilReq = new AssetUtilizationRequest(
                assetId, date, BigDecimal.valueOf(8.5), BigDecimal.valueOf(100), "Daily route"
        );

        mockMvc.perform(post("/api/v1/fixed-assets/" + assetId + "/utilization")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(utilReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.hoursUsed").value(8.5));

        // Get TCO
        mockMvc.perform(get("/api/v1/fixed-assets/" + assetId + "/tco")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.acquisitionCost").value(30000.00))
                .andExpect(jsonPath("$.data.totalCostOfOwnership").value(30000.00));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "FIXED_ASSET_VIEW", "FIXED_ASSET_CREATE", "FIXED_ASSET_ACQUIRE", "FIXED_ASSET_REVALUE", "FIXED_ASSET_IMPAIR", "FIXED_ASSET_LEASE"
    })
    public void testRevaluationImpairmentAndLeases() throws Exception {
        LocalDate date = LocalDate.now();

        // Register and capitalize asset
        FixedAssetRequest assetReq = new FixedAssetRequest(
                category.getId(), null, "HQ Building", "Headquarters",
                date, BigDecimal.valueOf(500000.00), BigDecimal.ZERO, 25,
                BigDecimal.valueOf(4.00), "STRAIGHT_LINE", warehouse.getId(), null, null
        );

        String assetJson = mockMvc.perform(post("/api/v1/fixed-assets")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assetReq)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long assetId = objectMapper.readTree(assetJson).get("data").get("id").asLong();

        mockMvc.perform(post("/api/v1/fixed-assets/" + assetId + "/acquire")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk());

        // 1. Revalue asset
        AssetRevaluationRequest revalReq = new AssetRevaluationRequest(
                date, BigDecimal.valueOf(550000.00), assetGLAccount.getId(), "Market appreciation"
        );
        mockMvc.perform(post("/api/v1/fixed-assets/" + assetId + "/revalue")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(revalReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.newFairValue").value(550000.00));

        // 2. Impair asset
        AssetImpairmentRequest impairReq = new AssetImpairmentRequest(
                date, BigDecimal.valueOf(20000.00), BigDecimal.valueOf(530000.00), "Slight damage"
        );
        mockMvc.perform(post("/api/v1/fixed-assets/" + assetId + "/impair")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(impairReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.impairmentAmount").value(20000.00));

        // 3. Create Lease
        AssetLeaseRequest leaseReq = new AssetLeaseRequest(
                "FINANCE", date, date.plusYears(5), BigDecimal.valueOf(5000.00), "Lessor Corp", assetGLAccount.getId()
        );
        mockMvc.perform(post("/api/v1/fixed-assets/" + assetId + "/leases")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(leaseReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.lessorName").value("Lessor Corp"));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "FIXED_ASSET_VIEW", "FIXED_ASSET_CREATE", "FIXED_ASSET_ACQUIRE", "FIXED_ASSET_TRANSFER", "FIXED_ASSET_TRANSFER_APPROVE"
    })
    public void testAssetTransferApprovalWorkflow() throws Exception {
        LocalDate date = LocalDate.now();

        // Register and capitalize asset
        FixedAssetRequest assetReq = new FixedAssetRequest(
                category.getId(), null, "Transit Crane", "Mobile crane",
                date, BigDecimal.valueOf(45000.00), BigDecimal.ZERO, 5,
                BigDecimal.valueOf(20.00), "STRAIGHT_LINE", warehouse.getId(), null, null
        );

        String assetJson = mockMvc.perform(post("/api/v1/fixed-assets")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assetReq)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long assetId = objectMapper.readTree(assetJson).get("data").get("id").asLong();

        mockMvc.perform(post("/api/v1/fixed-assets/" + assetId + "/acquire")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk());

        // 1. Request transfer
        AssetTransferRequest transferReq = new AssetTransferRequest(
                date, null, store.getId(), company.getId(), "Inter-site transfer"
        );
        String transferJson = mockMvc.perform(post("/api/v1/fixed-assets/" + assetId + "/transfer")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("REQUESTED"))
                .andReturn().getResponse().getContentAsString();
        Long transferId = objectMapper.readTree(transferJson).get("data").get("id").asLong();

        // 2. Approve transfer
        mockMvc.perform(post("/api/v1/fixed-assets/transfers/" + transferId + "/approve")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("MANAGER_APPROVED"));

        // 3. Receive transfer
        mockMvc.perform(post("/api/v1/fixed-assets/transfers/" + transferId + "/receive")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {
            "FIXED_ASSET_VIEW", "FIXED_ASSET_CREATE", "FIXED_ASSET_ACQUIRE", "FIXED_ASSET_COMPLIANCE"
    })
    public void testLegalHoldsAndHierarchyRollup() throws Exception {
        LocalDate date = LocalDate.now();

        // 1. Register and capitalize nested assets
        // Parent: Campus
        FixedAssetRequest parentReq = new FixedAssetRequest(
                category.getId(), null, "Dubai Campus", "Dubai Campus HQ",
                date, BigDecimal.valueOf(100000.00), BigDecimal.ZERO, 10,
                BigDecimal.valueOf(10.00), "STRAIGHT_LINE", warehouse.getId(), null, null
        );
        String parentJson = mockMvc.perform(post("/api/v1/fixed-assets")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(parentReq)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long parentId = objectMapper.readTree(parentJson).get("data").get("id").asLong();
        mockMvc.perform(post("/api/v1/fixed-assets/" + parentId + "/acquire")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk());

        // Child: Building
        FixedAssetRequest childReq = new FixedAssetRequest(
                category.getId(), parentId, "Building A", "Administration Building",
                date, BigDecimal.valueOf(50000.00), BigDecimal.ZERO, 10,
                BigDecimal.valueOf(10.00), "STRAIGHT_LINE", warehouse.getId(), null, null
        );
        String childJson = mockMvc.perform(post("/api/v1/fixed-assets")
                        .param("companyId", company.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(childReq)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long childId = objectMapper.readTree(childJson).get("data").get("id").asLong();
        mockMvc.perform(post("/api/v1/fixed-assets/" + childId + "/acquire")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk());

        // 2. Fetch hierarchy rollup
        mockMvc.perform(get("/api/v1/fixed-assets/" + parentId + "/rollup")
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalAssetValue").value(150000.00));

        // 3. Toggle Legal Hold on child
        mockMvc.perform(post("/api/v1/fixed-assets/" + childId + "/legal-hold")
                        .param("companyId", company.getId().toString())
                        .param("hold", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isLegalHold").value(true));

        // 4. Assert soft delete or transfer is BLOCKED due to legal hold
        mockMvc.perform(delete("/api/v1/fixed-assets/" + childId)
                        .param("companyId", company.getId().toString()))
                .andExpect(status().isBadRequest());
    }
}
