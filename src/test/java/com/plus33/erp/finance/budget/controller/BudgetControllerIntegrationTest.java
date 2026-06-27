package com.plus33.erp.finance.budget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.finance.assets.dto.FixedAssetRequest;
import com.plus33.erp.finance.assets.dto.FixedAssetResponse;
import com.plus33.erp.finance.assets.entity.AssetCategory;
import com.plus33.erp.finance.assets.entity.DepreciationMethod;
import com.plus33.erp.finance.assets.repository.AssetCategoryRepository;
import com.plus33.erp.finance.assets.service.FixedAssetService;
import com.plus33.erp.finance.budget.dto.*;
import com.plus33.erp.finance.budget.entity.*;
import com.plus33.erp.finance.budget.repository.*;
import com.plus33.erp.finance.budget.scheduler.BudgetReservationExpiryJob;
import com.plus33.erp.finance.dto.*;
import com.plus33.erp.finance.entity.*;
import com.plus33.erp.finance.repository.*;
import com.plus33.erp.finance.reporting.entity.FiscalYear;
import com.plus33.erp.finance.reporting.entity.FiscalYearStatus;
import com.plus33.erp.finance.reporting.repository.FiscalYearRepository;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.repository.ProductRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.organization.repository.StoreRepository;
import com.plus33.erp.organization.repository.WarehouseRepository;
import com.plus33.erp.procurement.dto.*;
import com.plus33.erp.procurement.entity.*;
import com.plus33.erp.procurement.repository.PurchaseOrderItemRepository;
import com.plus33.erp.procurement.repository.PurchaseOrderRepository;
import com.plus33.erp.procurement.repository.PurchaseRequestRepository;
import com.plus33.erp.procurement.repository.SupplierRepository;
import com.plus33.erp.procurement.service.PurchaseOrderService;
import com.plus33.erp.procurement.service.PurchaseRequestService;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BudgetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private FiscalYearRepository fiscalYearRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CostCenterRepository costCenterRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BudgetPolicyRepository budgetPolicyRepository;

    @Autowired
    private BudgetWorkflowTemplateRepository workflowTemplateRepository;

    @Autowired
    private BudgetWorkflowStepRepository workflowStepRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetVersionRepository budgetVersionRepository;

    @Autowired
    private BudgetLineRepository budgetLineRepository;

    @Autowired
    private BudgetControlCacheRepository budgetControlCacheRepository;

    @Autowired
    private BudgetReservationRepository budgetReservationRepository;

    @Autowired
    private BudgetConsumptionRepository budgetConsumptionRepository;

    @Autowired
    private BudgetDimensionSetRepository budgetDimensionSetRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private PurchaseRequestRepository purchaseRequestRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private PurchaseOrderItemRepository purchaseOrderItemRepository;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private AssetCategoryRepository assetCategoryRepository;

    @Autowired
    private FixedAssetService fixedAssetService;

    @Autowired
    private PurchaseRequestService purchaseRequestService;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private BudgetReservationExpiryJob expiryJob;

    @Autowired
    private ObjectMapper objectMapper;

    private Company company;
    private FiscalYear fiscalYear;
    private Account expenseAccount;
    private Account assetAccount;
    private User adminUser;
    private Product product;
    private Supplier supplier;
    private Warehouse warehouse;

    @BeforeEach
    public void setUp() {
        company = companyRepository.findByCode("PLUS33_GLOBAL")
                .orElseThrow(() -> new AssertionError("PLUS33_GLOBAL company not found"));

        adminUser = userRepository.findByEmail("admin@plus33.com")
                .orElseThrow(() -> new AssertionError("admin@plus33.com user not found"));

        // Load expense and asset accounts
        expenseAccount = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "5200")
                .orElseThrow(() -> new AssertionError("Expense account 5200 not found"));
        assetAccount = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "1300")
                .orElseThrow(() -> new AssertionError("Asset account 1300 not found"));

        // Setup/Get target fiscal year
        fiscalYear = fiscalYearRepository.findAll().stream()
                .filter(fy -> fy.getCompany().getId().equals(company.getId()))
                .findFirst()
                .orElseGet(() -> {
                    FiscalYear fy = FiscalYear.builder()
                            .company(company)
                            .fiscalYear(LocalDate.now().getYear())
                            .startDate(LocalDate.of(LocalDate.now().getYear(), 1, 1))
                            .endDate(LocalDate.of(LocalDate.now().getYear(), 12, 31))
                            .status(FiscalYearStatus.OPEN)
                            .build();
                    return fiscalYearRepository.save(fy);
                });

        product = productRepository.findAll().stream()
                .filter(p -> p.getActive() && "EXPENSE".equalsIgnoreCase(p.getProductType()))
                .findFirst()
                .orElseGet(() -> {
                    Product template = productRepository.findAll().stream().findFirst().orElseThrow(() -> new AssertionError("No product found"));
                    Product p = Product.builder()
                            .code("TEST_PROD_EXPENSE_" + System.nanoTime())
                            .name("Test Expense Product")
                            .category(template.getCategory())
                            .unit(template.getUnit())
                            .productType("EXPENSE")
                            .active(true)
                            .build();
                    return productRepository.save(p);
                });

        supplier = supplierRepository.findAll().stream()
                .filter(s -> s.getCompany().getId().equals(company.getId()) && s.getActive())
                .findFirst()
                .orElseGet(() -> {
                    Supplier s = Supplier.builder()
                            .company(company)
                            .code("TEST_SUPP")
                            .name("Test Supplier")
                            .active(true)
                            .build();
                    return supplierRepository.save(s);
                });

        warehouse = warehouseRepository.findByCode("DUBAI_WAREHOUSE")
                .orElseThrow(() -> new AssertionError("DUBAI_WAREHOUSE not found"));
    }

    @Test
    @WithMockUser(username = "admin@plus33.com", authorities = {"BUDGET_CREATE", "BUDGET_VIEW", "BUDGET_APPROVE", "BUDGET_LOCK", "BUDGET_REVISE", "BUDGET_FREEZE", "FINANCE_WRITE"})
    public void testBudgetWorkflowAndIntegrations() throws Exception {
        // 1. Budget Administration setup
        DepartmentRequest deptReq = new DepartmentRequest("DEPT_IT", "Information Technology", true);
        String deptJson = mockMvc.perform(post("/api/v1/departments?companyId=" + company.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deptReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        DepartmentResponse deptRes = objectMapper.readValue(deptJson, ApiResponse.class).data() != null ?
                objectMapper.convertValue(objectMapper.readValue(deptJson, ApiResponse.class).data(), DepartmentResponse.class) : null;
        assertNotNull(deptRes);

        CostCenterRequest ccReq = new CostCenterRequest("CC_IT_OPS", "IT Operations", true);
        String ccJson = mockMvc.perform(post("/api/v1/cost-centers?companyId=" + company.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ccReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        CostCenterResponse ccRes = objectMapper.readValue(ccJson, ApiResponse.class).data() != null ?
                objectMapper.convertValue(objectMapper.readValue(ccJson, ApiResponse.class).data(), CostCenterResponse.class) : null;
        assertNotNull(ccRes);

        ProjectRequest projReq = new ProjectRequest("PROJ_ERP", "ERP Deployment", LocalDate.now(), LocalDate.now().plusMonths(6), "ACTIVE", true);
        String projJson = mockMvc.perform(post("/api/v1/projects?companyId=" + company.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        ProjectResponse projRes = objectMapper.readValue(projJson, ApiResponse.class).data() != null ?
                objectMapper.convertValue(objectMapper.readValue(projJson, ApiResponse.class).data(), ProjectResponse.class) : null;
        assertNotNull(projRes);

        BudgetPolicyRequest policyReq = new BudgetPolicyRequest("POL_IT", "IT Budget Policy", "HARD", false, true, true, true, true, true, false, true);
        String policyJson = mockMvc.perform(post("/api/v1/budget-policies?companyId=" + company.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(policyReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        BudgetPolicyResponse policyRes = objectMapper.readValue(policyJson, ApiResponse.class).data() != null ?
                objectMapper.convertValue(objectMapper.readValue(policyJson, ApiResponse.class).data(), BudgetPolicyResponse.class) : null;
        assertNotNull(policyRes);

        List<BudgetWorkflowStepRequest> steps = List.of(new BudgetWorkflowStepRequest(1, "ROLE_ADMIN", BigDecimal.ZERO, true));
        BudgetWorkflowTemplateRequest wfReq = new BudgetWorkflowTemplateRequest("WF_IT", "IT Workflow", true, steps);
        String wfJson = mockMvc.perform(post("/api/v1/budget-workflows?companyId=" + company.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wfReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        BudgetWorkflowTemplateResponse wfRes = objectMapper.readValue(wfJson, ApiResponse.class).data() != null ?
                objectMapper.convertValue(objectMapper.readValue(wfJson, ApiResponse.class).data(), BudgetWorkflowTemplateResponse.class) : null;
        assertNotNull(wfRes);

        // 2. Budget Creation & Line Distribution
        CostCenterRequest ccReq2 = new CostCenterRequest("CC_IT_DEV", "IT Development", true);
        String cc2Json = mockMvc.perform(post("/api/v1/cost-centers?companyId=" + company.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ccReq2)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        CostCenterResponse ccRes2 = objectMapper.readValue(cc2Json, ApiResponse.class).data() != null ?
                objectMapper.convertValue(objectMapper.readValue(cc2Json, ApiResponse.class).data(), CostCenterResponse.class) : null;
        assertNotNull(ccRes2);

        BudgetDimensionSetRequest dimSetReq = new BudgetDimensionSetRequest(deptRes.id(), ccRes.id(), projRes.id(), null, null, null, null);
        BudgetDimensionSetRequest dimSetReq2 = new BudgetDimensionSetRequest(deptRes.id(), ccRes2.id(), projRes.id(), null, null, null, null);
        BudgetLineRequest lineReq = new BudgetLineRequest(expenseAccount.getId(), dimSetReq, LocalDate.now().withDayOfMonth(1), LocalDate.now().withDayOfMonth(1).plusMonths(1).minusDays(1), BigDecimal.valueOf(10000.00), "EQUAL", null, "IT Expense Line 1");
        BudgetLineRequest line1bReq = new BudgetLineRequest(expenseAccount.getId(), dimSetReq2, LocalDate.now().withDayOfMonth(1), LocalDate.now().withDayOfMonth(1).plusMonths(1).minusDays(1), BigDecimal.valueOf(5000.00), "EQUAL", null, "IT Expense Line 2");
        BudgetRequest budgetReq = new BudgetRequest(fiscalYear.getId(), policyRes.id(), wfRes.id(), "B_IT_2026", "IT Operating Budget 2026", "OPERATING", "MONTHLY", "EXPECTED", false, null, null, "SPOT", BigDecimal.ONE, List.of(lineReq, line1bReq));

        String bJson = mockMvc.perform(post("/api/v1/budgets?companyId=" + company.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(budgetReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        BudgetResponse budgetRes = objectMapper.readValue(bJson, ApiResponse.class).data() != null ?
                objectMapper.convertValue(objectMapper.readValue(bJson, ApiResponse.class).data(), BudgetResponse.class) : null;
        assertNotNull(budgetRes);
        assertEquals(2, budgetRes.lines().size());
        assertEquals(0, BigDecimal.valueOf(10000.00).compareTo(budgetRes.lines().get(0).allocatedAmount()));

        // 3. Workflow Approvals Sequence
        String submitJson = mockMvc.perform(post("/api/v1/budgets/" + budgetRes.id() + "/submit"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        BudgetResponse submittedRes = objectMapper.readValue(submitJson, ApiResponse.class).data() != null ?
                objectMapper.convertValue(objectMapper.readValue(submitJson, ApiResponse.class).data(), BudgetResponse.class) : null;
        assertEquals("SUBMITTED", submittedRes.status());

        String approveJson = mockMvc.perform(post("/api/v1/budgets/" + budgetRes.id() + "/approve?remarks=Approved"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        BudgetResponse approvedRes = objectMapper.readValue(approveJson, ApiResponse.class).data() != null ?
                objectMapper.convertValue(objectMapper.readValue(approveJson, ApiResponse.class).data(), BudgetResponse.class) : null;
        assertEquals("APPROVED", approvedRes.status());

        // Verify Budget Control Cache has O(1) balances synced
        BudgetLineResponse activeLine = approvedRes.lines().get(0);
        BudgetLineResponse activeLine2 = approvedRes.lines().get(1);
        BudgetControlCache cache = budgetControlCacheRepository.findById(activeLine.id()).orElse(null);
        assertNotNull(cache);
        assertEquals(0, BigDecimal.valueOf(10000.00).compareTo(cache.getAllocatedAmount()));
        assertEquals(0, BigDecimal.ZERO.compareTo(cache.getReservedAmount()));
        assertEquals(0, BigDecimal.ZERO.compareTo(cache.getConsumedAmount()));
        assertEquals(0, BigDecimal.valueOf(10000.00).compareTo(cache.getAvailableAmount()));

        // 4. Revisions & Re-allocations
        BudgetRevisionRequest revReq = new BudgetRevisionRequest(activeLine.id(), LocalDate.now(), BigDecimal.valueOf(12000.00), "Revision increase");
        mockMvc.perform(post("/api/v1/budgets/revisions?companyId=" + company.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(revReq)))
                .andExpect(status().isOk());

        BudgetControlCache cacheAfterRev = budgetControlCacheRepository.findById(activeLine.id()).orElse(null);
        assertEquals(0, BigDecimal.valueOf(12000.00).compareTo(cacheAfterRev.getAllocatedAmount()));

        // Lock Status Check
        mockMvc.perform(post("/api/v1/budgets/lines/" + activeLine.id() + "/lock?isLocked=true"))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/v1/budgets/lines/" + activeLine.id() + "/lock?isLocked=false"))
                .andExpect(status().isOk());

        // 5. Budget Transfer
        mockMvc.perform(post("/api/v1/budgets/transfers?companyId=" + company.getId()
                + "&sourceLineId=" + activeLine.id()
                + "&targetLineId=" + activeLine2.id()
                + "&amount=2000.00&reason=Transfer"))
                .andExpect(status().isOk());

        assertEquals(0, BigDecimal.valueOf(10000.00).compareTo(budgetControlCacheRepository.findById(activeLine.id()).get().getAllocatedAmount()));
        assertEquals(0, BigDecimal.valueOf(7000.00).compareTo(budgetControlCacheRepository.findById(activeLine2.id()).get().getAllocatedAmount()));

        // Create second budget for comparison test
        BudgetDimensionSetRequest targetDimSet = new BudgetDimensionSetRequest(deptRes.id(), ccRes.id(), projRes.id(), null, null, null, null);
        BudgetLineRequest line2Req = new BudgetLineRequest(expenseAccount.getId(), targetDimSet, LocalDate.now().withDayOfMonth(1), LocalDate.now().withDayOfMonth(1).plusMonths(1).minusDays(1), BigDecimal.valueOf(5000.00), "EQUAL", null, "IT Line 2");
        BudgetRequest budget2Req = new BudgetRequest(fiscalYear.getId(), policyRes.id(), wfRes.id(), "B_IT_2026_V2", "IT Operating Budget 2026 V2", "OPERATING", "MONTHLY", "EXPECTED", false, null, null, "SPOT", BigDecimal.ONE, List.of(line2Req));

        String b2Json = mockMvc.perform(post("/api/v1/budgets?companyId=" + company.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(budget2Req)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        BudgetResponse budget2Res = objectMapper.readValue(b2Json, ApiResponse.class).data() != null ?
                objectMapper.convertValue(objectMapper.readValue(b2Json, ApiResponse.class).data(), BudgetResponse.class) : null;


        // Side-by-side variance analysis
        mockMvc.perform(get("/api/v1/budgets/compare?companyId=" + company.getId() + "&budgetId1=" + budgetRes.id() + "&budgetId2=" + budget2Res.id()))
                .andExpect(status().isOk());

        // 6. Encumbrance Lifecycle (PR -> PO -> Invoice)
        BudgetDimensionSet ds = budgetDimensionSetRepository.findAll().stream()
                .filter(d -> d.getDepartment() != null && d.getDepartment().getId().equals(deptRes.id()))
                .findFirst().orElseThrow(() -> new AssertionError("Dimension set not found"));

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                "admin@plus33.com",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        PurchaseRequestRequest prRequest = new PurchaseRequestRequest(
                company.getId(),
                supplier.getId(),
                warehouse.getId(),
                null,
                LocalDate.now().plusDays(10),
                "PR notes",
                List.of(new PurchaseRequestItemRequest(product.getId(), BigDecimal.TEN, "PCS", "PR Item", ds.getId()))
        );

        PurchaseRequestResponse prRes = purchaseRequestService.createPurchaseRequest(prRequest);
        assertNotNull(prRes);
        PurchaseRequestResponse prSubmitted = purchaseRequestService.submitPurchaseRequest(prRes.id());
        assertEquals(PurchaseRequestStatus.SUBMITTED, prSubmitted.status());

        // Check cache reserved amount
        BigDecimal expectedPRReserve = BigDecimal.TEN.multiply(BigDecimal.valueOf(100.00));
        assertEquals(0, expectedPRReserve.compareTo(budgetControlCacheRepository.findById(activeLine.id()).get().getReservedAmount()));

        // Approve PR
        PurchaseRequestResponse prApproved = purchaseRequestService.approvePurchaseRequest(prSubmitted.id());
        assertEquals(PurchaseRequestStatus.APPROVED, prApproved.status());

        // PO conversion and approval
        PurchaseRequestResponse convertedRes = purchaseRequestService.convertPurchaseRequestToPo(prApproved.id());
        PurchaseOrderResponse poRes = purchaseOrderService.getPurchaseOrderById(convertedRes.purchaseOrderId());
        assertNotNull(poRes);

        // Convert PO item requests
        List<PurchaseOrderItemRequest> poItemRequests = List.of(new PurchaseOrderItemRequest(product.getId(), BigDecimal.TEN, BigDecimal.valueOf(150.00), "PO Item", ds.getId()));
        PurchaseOrderRequest poUpdateReq = new PurchaseOrderRequest(
                company.getId(),
                supplier.getId(),
                prSubmitted.id(),
                LocalDate.now().plusDays(10),
                "PO notes",
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                "AED",
                poItemRequests
        );
        purchaseOrderService.updatePurchaseOrder(poRes.id(), poUpdateReq);
        PurchaseOrderResponse poApproved = purchaseOrderService.approvePurchaseOrder(poRes.id());
        assertEquals(PurchaseOrderStatus.ISSUED, poApproved.status());

        // PR reservation released and PO reservation created (10 * 150 = 1500)
        assertEquals(0, BigDecimal.valueOf(1500.00).compareTo(budgetControlCacheRepository.findById(activeLine.id()).get().getReservedAmount()));

        // Supplier Invoice Approval & Consumption transition
        SupplierInvoiceRequest invoiceRequest = new SupplierInvoiceRequest();
        invoiceRequest.setCompanyId(company.getId());
        invoiceRequest.setSupplierId(supplier.getId());
        invoiceRequest.setPurchaseOrderId(poApproved.id());
        invoiceRequest.setInvoiceNumber("INV-TEST-BUDGET-" + System.nanoTime());
        invoiceRequest.setInvoiceDate(LocalDate.now());
        invoiceRequest.setDueDate(LocalDate.now().plusDays(30));
        invoiceRequest.setCurrencyCode("AED");

        SupplierInvoiceItemRequest invItem = new SupplierInvoiceItemRequest();
        invItem.setPurchaseOrderItemId(poApproved.items().get(0).id());
        invItem.setQuantity(BigDecimal.TEN);
        invItem.setUnitPrice(BigDecimal.valueOf(150.00));
        invItem.setTaxRate(BigDecimal.ZERO);
        invItem.setDiscountAmount(BigDecimal.ZERO);
        invItem.setDimensionSetId(ds.getId());
        invoiceRequest.setItems(List.of(invItem));

        // 7. Direct GL postings
        JournalEntryRequest jeReq = new JournalEntryRequest(
                company.getId(),
                LocalDate.now(),
                "Direct GL manual entry",
                "AED",
                List.of(
                        new JournalEntryLineRequest(expenseAccount.getId(), BigDecimal.valueOf(500.00), BigDecimal.ZERO, ds.getId()),
                        new JournalEntryLineRequest(accountRepository.findByCompanyIdAndAccountCode(company.getId(), "2100").get().getId(), BigDecimal.ZERO, BigDecimal.valueOf(500.00), null)
                )
        );

        String jeCreateJson = mockMvc.perform(post("/api/v1/journal-entries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jeReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        JournalEntryResponse jeRes = objectMapper.readValue(jeCreateJson, ApiResponse.class).data() != null ?
                objectMapper.convertValue(objectMapper.readValue(jeCreateJson, ApiResponse.class).data(), JournalEntryResponse.class) : null;

        mockMvc.perform(post("/api/v1/journal-entries/" + jeRes.id() + "/post"))
                .andExpect(status().isOk());

        // Verify consumption increased by 500.00
        assertEquals(0, BigDecimal.valueOf(500.00).compareTo(budgetControlCacheRepository.findById(activeLine.id()).get().getConsumedAmount()));

        // 8. Fixed Asset CapEx Checks
        AssetCategory category = assetCategoryRepository.findAll().stream()
                .filter(c -> c.getCompany().getId().equals(company.getId()))
                .findFirst()
                .orElseGet(() -> {
                    AssetCategory ac = AssetCategory.builder()
                            .company(company)
                            .code("CAT_IT")
                            .name("IT Hardware")
                            .depreciationMethod(DepreciationMethod.STRAIGHT_LINE)
                            .depreciationRate(BigDecimal.valueOf(20.00))
                            .usefulLifeYears(5)
                            .assetAccount(assetAccount)
                            .accumulatedDepreciationAccount(accountRepository.findByCompanyIdAndAccountCode(company.getId(), "1300").get())
                            .depreciationExpenseAccount(expenseAccount)
                            .gainLossAccount(expenseAccount)
                            .capitalizationThreshold(BigDecimal.valueOf(1000.00))
                            .build();
                    return assetCategoryRepository.save(ac);
                });

        // Setup CapEx Budget for assetAccount (1300) and this category
        BudgetDimensionSetRequest capExDimReq = new BudgetDimensionSetRequest(null, null, null, null, category.getId(), null, null);
        BudgetLineRequest capExLine = new BudgetLineRequest(assetAccount.getId(), capExDimReq, LocalDate.now().withDayOfMonth(1), LocalDate.now().withDayOfMonth(1).plusMonths(1).minusDays(1), BigDecimal.valueOf(20000.00), "EQUAL", null, "IT CapEx Hardware");
        BudgetRequest capExBudgetReq = new BudgetRequest(fiscalYear.getId(), policyRes.id(), wfRes.id(), "B_CAP_IT", "IT CapEx Budget", "CAPITAL", "MONTHLY", "EXPECTED", false, null, null, "SPOT", BigDecimal.ONE, List.of(capExLine));

        String capExBJson = mockMvc.perform(post("/api/v1/budgets?companyId=" + company.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(capExBudgetReq)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        BudgetResponse capExBudgetRes = objectMapper.readValue(capExBJson, ApiResponse.class).data() != null ?
                objectMapper.convertValue(objectMapper.readValue(capExBJson, ApiResponse.class).data(), BudgetResponse.class) : null;

        mockMvc.perform(post("/api/v1/budgets/" + capExBudgetRes.id() + "/submit")).andExpect(status().isOk());
        mockMvc.perform(post("/api/v1/budgets/" + capExBudgetRes.id() + "/approve?remarks=Approved")).andExpect(status().isOk());

        BudgetLineResponse activeCapExLine = capExBudgetRes.lines().get(0);

        FixedAssetRequest assetReq = new FixedAssetRequest(
                category.getId(),
                null,
                "MacBook Pro M3",
                "Development laptops",
                LocalDate.now(),
                BigDecimal.valueOf(4500.00),
                BigDecimal.ZERO,
                5,
                BigDecimal.valueOf(20.00),
                "STRAIGHT_LINE",
                null,
                null,
                null
        );

        FixedAssetResponse assetRes = fixedAssetService.registerAsset(company.getId(), assetReq, "admin@plus33.com");
        FixedAssetResponse acquiredAsset = fixedAssetService.acquireAsset(company.getId(), assetRes.id(), "admin@plus33.com");
        assertEquals("ACTIVE", acquiredAsset.status());

        // Verify CapEx budget direct consumption (4500.00)
        assertEquals(0, BigDecimal.valueOf(4500.00).compareTo(budgetControlCacheRepository.findById(activeCapExLine.id()).get().getConsumedAmount()));

        // 9. Sweeper Cron Expiry Job Check
        BudgetReservation expiredRes = BudgetReservation.builder()
                .budgetLine(budgetLineRepository.findById(activeLine.id()).get())
                .sourceModule("MOCK_EXPIRY")
                .sourceReferenceId(9999L)
                .referenceNumber("EXP-001")
                .reservedAmount(BigDecimal.valueOf(100.00))
                .status(ReservationStatus.ACTIVE)
                .expiryDate(LocalDate.now().minusDays(1)) // Expired yesterday
                .build();
        budgetReservationRepository.save(expiredRes);

        // Adjust budget line reservation amount manually to simulate active reservation amount
        BudgetLine bl = budgetLineRepository.findById(activeLine.id()).get();
        bl.setReservedAmount(bl.getReservedAmount().add(BigDecimal.valueOf(100.00)));
        budgetLineRepository.save(bl);

        expiryJob.sweepExpiredReservations();

        // Check reservation status becomes RELEASED
        BudgetReservation updatedExpiredRes = budgetReservationRepository.findById(expiredRes.getId()).get();
        assertEquals(ReservationStatus.RELEASED, updatedExpiredRes.getStatus());

        // 10. Materialized View Drilldown Checks
        mockMvc.perform(get("/api/v1/budgets/" + budgetRes.id() + "/drilldown?companyId=" + company.getId() + "&dimensionName=DEPARTMENT"))
                .andExpect(status().isOk());
    }
}
