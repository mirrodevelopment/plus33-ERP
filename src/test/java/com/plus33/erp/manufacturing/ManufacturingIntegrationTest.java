package com.plus33.erp.manufacturing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plus33.erp.inventory.entity.InventoryStock;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.ProductCategory;
import com.plus33.erp.inventory.entity.UnitOfMeasure;
import com.plus33.erp.organization.entity.Region;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.inventory.repository.InventoryStockRepository;
import com.plus33.erp.inventory.repository.ProductCategoryRepository;
import com.plus33.erp.inventory.repository.ProductRepository;
import com.plus33.erp.inventory.repository.UnitOfMeasureRepository;
import com.plus33.erp.organization.repository.RegionRepository;
import com.plus33.erp.organization.repository.WarehouseRepository;
import com.plus33.erp.manufacturing.dto.*;
import com.plus33.erp.manufacturing.entity.*;
import com.plus33.erp.manufacturing.repository.*;
import com.plus33.erp.manufacturing.service.*;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
@TestPropertySource(properties = { "spring.flyway.enabled=false", "spring.jpa.hibernate.ddl-auto=update" })
public class ManufacturingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private InventoryStockRepository inventoryStockRepository;

    @Autowired
    private WorkCenterService workCenterService;

    @Autowired
    private BomService bomService;

    @Autowired
    private BomHeaderRepository bomHeaderRepository;

    @Autowired
    private ProductionOrderService productionOrderService;

    @Autowired
    private MrpService mrpService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EngineeringChangeService ecoService;

    @Autowired
    private QualityInspectionService qualityService;

    @Autowired
    private ManufacturingCalendarService calendarService;

    @Autowired
    private ProductionConfirmationRepository confirmationRepository;

    @Autowired
    private ProductionScrapRepository scrapRepository;

    @Autowired
    private ProductionReworkRepository reworkRepository;

    @Autowired
    private ManufacturingBatchGenealogyRepository batchGenealogyRepository;

    @Autowired
    private ManufacturingEventRepository eventRepository;

    private Company testCompany;
    private Product finishedGoods;
    private Product rawMaterial;
    private UnitOfMeasure uom;
    private Warehouse testWarehouse;

    @BeforeEach
    void setUp() {
        testCompany = companyRepository.findByCode("MFG-COMP").orElseGet(() -> {
            Company c = new Company();
            c.setCode("MFG-COMP");
            c.setName("Manufacturing Test Co");
            return companyRepository.save(c);
        });

        // Setup Accounts for Manufacturing Cost Variance Settlements
        if (!accountRepository.findByCompanyIdAndAccountCode(testCompany.getId(), "1400").isPresent()) {
            Account wipAccount = Account.builder()
                    .company(testCompany)
                    .accountCode("1400")
                    .accountName("Work in Progress")
                    .accountType("ASSET")
                    .active(true)
                    .build();
            accountRepository.save(wipAccount);
        }

        if (!accountRepository.findByCompanyIdAndAccountCode(testCompany.getId(), "5500").isPresent()) {
            Account varianceAccount = Account.builder()
                    .company(testCompany)
                    .accountCode("5500")
                    .accountName("Manufacturing Variance")
                    .accountType("EXPENSE")
                    .active(true)
                    .build();
            accountRepository.save(varianceAccount);
        }

        final Region region = regionRepository.findByCode("REG-TEST-01").orElseGet(() -> {
            Region r = new Region();
            r.setCode("REG-TEST-01");
            r.setName("Test Region");
            r.setCompany(testCompany);
            return regionRepository.save(r);
        });

        testWarehouse = warehouseRepository.findByCode("WH-TEST-01").orElseGet(() -> {
            Warehouse w = new Warehouse();
            w.setCode("WH-TEST-01");
            w.setName("Test Warehouse");
            w.setRegion(region);
            return warehouseRepository.save(w);
        });

        final ProductCategory cat = productCategoryRepository.findByCode("CAT-MFG").orElseGet(() -> {
            ProductCategory c = new ProductCategory();
            c.setCode("CAT-MFG");
            c.setName("Manufacturing Goods");
            return productCategoryRepository.save(c);
        });

        uom = unitOfMeasureRepository.findByCode("PCS").orElseGet(() -> {
            UnitOfMeasure u = new UnitOfMeasure();
            u.setCode("PCS");
            u.setName("Pieces");
            return unitOfMeasureRepository.save(u);
        });

        finishedGoods = productRepository.findByCode("FG-WIDGET-01").orElseGet(() -> {
            Product p = new Product();
            p.setCode("FG-WIDGET-01");
            p.setName("Super Deluxe Widget");
            p.setCategory(cat);
            p.setUnit(uom);
            p.setProductType("FINISHED_GOODS");
            return productRepository.save(p);
        });

        rawMaterial = productRepository.findByCode("RM-STEEL-01").orElseGet(() -> {
            Product p = new Product();
            p.setCode("RM-STEEL-01");
            p.setName("Steel Sheet Grade A");
            p.setCategory(cat);
            p.setUnit(uom);
            p.setProductType("RAW_MATERIAL");
            return productRepository.save(p);
        });
    }

    @Test
    @WithMockUser(authorities = {"MANUFACTURING_MANAGE", "MANUFACTURING_VIEW", "PRODUCTION_RELEASE", "PRODUCTION_EXECUTE", "QUALITY_APPROVE"})
    void testFullManufacturingLifecycle() throws Exception {
        // 1. Create Work Center
        CreateWorkCenterRequest wcReq = new CreateWorkCenterRequest();
        wcReq.setCompanyId(testCompany.getId());
        wcReq.setCode("WC-ASSY-01");
        wcReq.setName("Assembly Station 1");
        wcReq.setHourlyLaborRate(new BigDecimal("45.00"));
        wcReq.setHourlyOverheadRate(new BigDecimal("20.00"));
        wcReq.setCapacityHoursPerDay(new BigDecimal("8.0"));

        WorkCenterDto wcDto = workCenterService.createWorkCenter(wcReq);
        assertNotNull(wcDto.getId());
        assertEquals("WC-ASSY-01", wcDto.getCode());

        // 2. Create BOM
        CreateBomRequest bomReq = new CreateBomRequest();
        bomReq.setCompanyId(testCompany.getId());
        bomReq.setProductId(finishedGoods.getId());
        bomReq.setUnitId(uom.getId());
        bomReq.setBomCode("BOM-FG-01");
        bomReq.setBaseQuantity(new BigDecimal("1.0000"));

        BomHeaderDto bomDto = bomService.createBom(bomReq);
        assertNotNull(bomDto.getId());

        CreateBomLineRequest lineReq = new CreateBomLineRequest();
        lineReq.setComponentProductId(rawMaterial.getId());
        lineReq.setQuantity(new BigDecimal("2.0000"));
        lineReq.setUnitId(uom.getId());
        lineReq.setScrapPercentage(new BigDecimal("2.50"));
        bomService.addBomLine(bomDto.getId(), lineReq);

        // Approve BOM
        BomHeaderDto approvedBom = bomService.approveBom(bomDto.getId(), "ENGINEER_BOB");
        assertEquals("APPROVED", approvedBom.getStatus());

        // Seed inventory stock for rawMaterial in testWarehouse
        InventoryStock seedStock = new InventoryStock();
        seedStock.setProduct(rawMaterial);
        seedStock.setWarehouse(testWarehouse);
        seedStock.setQuantity(new BigDecimal("200.0000"));
        seedStock.setReservedQuantity(BigDecimal.ZERO);
        inventoryStockRepository.save(seedStock);

        // 3. Create Production Order via Controller API
        CreateProductionOrderRequest poReq = new CreateProductionOrderRequest();
        poReq.setCompanyId(testCompany.getId());
        poReq.setOrderNumber("PO-2026-0001");
        poReq.setProductId(finishedGoods.getId());
        poReq.setUnitId(uom.getId());
        poReq.setBomHeaderId(approvedBom.getId());
        poReq.setPlannedQuantity(new BigDecimal("50.0000"));
        poReq.setPlannedStartDate(LocalDate.now());
        poReq.setPlannedEndDate(LocalDate.now().plusDays(5));
        poReq.setWarehouseId(testWarehouse.getId());

        CreateProductionOrderOperationRequest opReq = new CreateProductionOrderOperationRequest();
        opReq.setOperationNumber(10);
        opReq.setOperationName("Main Assembly");
        opReq.setWorkCenterId(wcDto.getId());
        opReq.setEstimatedSetupHours(new BigDecimal("1.00"));
        opReq.setEstimatedRunHours(new BigDecimal("5.00"));
        poReq.setOperations(List.of(opReq));

        String poJson = objectMapper.writeValueAsString(poReq);

        String responseStr = mockMvc.perform(post("/api/v1/manufacturing/production-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(poJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.orderNumber").value("PO-2026-0001"))
                .andExpect(jsonPath("$.data.status").value("PLANNED"))
                .andReturn().getResponse().getContentAsString();

        ProductionOrderDto poDto = objectMapper.readValue(
                objectMapper.readTree(responseStr).get("data").toString(),
                ProductionOrderDto.class
        );

        // 4. Release Production Order
        mockMvc.perform(put("/api/v1/manufacturing/production-orders/" + poDto.getId() + "/release"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("RELEASED"));

        // 5. Complete Operation
        Long opId = poDto.getOperations().get(0).getId();
        CompleteOperationRequest compOpReq = new CompleteOperationRequest();
        compOpReq.setActualSetupHours(new BigDecimal("1.20"));
        compOpReq.setActualRunHours(new BigDecimal("4.80"));
        compOpReq.setYieldQuantity(new BigDecimal("50.0000"));
        compOpReq.setScrapQuantity(BigDecimal.ZERO);

        mockMvc.perform(put("/api/v1/manufacturing/production-orders/" + poDto.getId() + "/operations/" + opId + "/complete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(compOpReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("COMPLETED"));

        // Verify Production Confirmation recorded
        List<ProductionConfirmation> confs = confirmationRepository.findByProductionOrderId(poDto.getId());
        assertEquals(1, confs.size());
        assertEquals(new BigDecimal("50.0000"), confs.get(0).getConfirmedQuantity());

        // 6. Complete Production Order
        mockMvc.perform(put("/api/v1/manufacturing/production-orders/" + poDto.getId() + "/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("COMPLETED"));

        // Verify production order finalized
        ProductionOrderDto finalPo = productionOrderService.getProductionOrderById(poDto.getId());
        assertEquals(ProductionOrderStatus.COMPLETED.name(), finalPo.getStatus());
        assertEquals(new BigDecimal("50.0000"), finalPo.getCompletedQuantity());

        // Verify Batch Genealogy exists
        List<ManufacturingBatchGenealogy> traceList = batchGenealogyRepository.findByProductionOrderId(poDto.getId());
        assertFalse(traceList.isEmpty());
        assertTrue(traceList.stream().anyMatch(g -> "OUTPUT".equals(g.getGenealogyType())));
        assertTrue(traceList.stream().anyMatch(g -> "INPUT".equals(g.getGenealogyType())));
    }

    @Test
    @WithMockUser(authorities = {"MANUFACTURING_MANAGE", "MANUFACTURING_VIEW"})
    void testMrpEngineExecution() throws Exception {
        // Create BOM if not exists so product is manufacturable
        if (!bomHeaderRepository.existsByCompanyIdAndBomNumber(testCompany.getId(), "BOM-MRP-01")) {
            CreateBomRequest bomReq = new CreateBomRequest();
            bomReq.setCompanyId(testCompany.getId());
            bomReq.setProductId(finishedGoods.getId());
            bomReq.setUnitId(finishedGoods.getUnit().getId());
            bomReq.setBomCode("BOM-MRP-01");
            bomReq.setBaseQuantity(BigDecimal.ONE);

            BomHeaderDto bom = bomService.createBom(bomReq);
            assertNotNull(bom.getId());
        }



        // Execute MRP run
        MrpRunRequest execReq = new MrpRunRequest();
        execReq.setCompanyId(testCompany.getId());
        execReq.setExecutedBy(1L);

        MrpRunRequest.DemandItem demand = new MrpRunRequest.DemandItem();
        demand.setProductId(finishedGoods.getId());
        demand.setQuantity(new BigDecimal("10.00"));
        demand.setDueDate(LocalDate.now().plusDays(10));
        demand.setSourceType("SALES_ORDER");
        demand.setSourceReferenceId(999L);
        execReq.setDemandItems(List.of(demand));

        mockMvc.perform(post("/api/v1/manufacturing/mrp/runs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(execReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Validate suggestions
        mockMvc.perform(get("/api/v1/manufacturing/mrp/suggestions/company/" + testCompany.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"BOM_MANAGE", "MANUFACTURING_VIEW"})
    void testEngineeringChangeOrderWorkflow() throws Exception {
        // 1. Create ECO
        CreateEcoRequest ecoReq = new CreateEcoRequest();
        ecoReq.setCompanyId(testCompany.getId());
        ecoReq.setEcoNumber("ECO-2026-0001");
        ecoReq.setTitle("Upgrade Steel Spec");
        ecoReq.setDescription("Replacing RM-STEEL-01 with Grade B sheets.");
        ecoReq.setReason("Improved durability requirements");
        ecoReq.setPriority("HIGH");
        ecoReq.setRequestedBy(1L);

        CreateEcoLineRequest line = new CreateEcoLineRequest();
        line.setChangeType("REPLACE");
        line.setReferenceType("BOM_HEADER");
        line.setReferenceId(1L);
        line.setNotes("Replace line 1");
        ecoReq.setLines(List.of(line));

        String ecoStr = mockMvc.perform(post("/api/v1/manufacturing/ecos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ecoReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.ecoNumber").value("ECO-2026-0001"))
                .andExpect(jsonPath("$.data.status").value("DRAFT"))
                .andReturn().getResponse().getContentAsString();

        EngineeringChangeOrderDto ecoDto = objectMapper.readValue(
                objectMapper.readTree(ecoStr).get("data").toString(),
                EngineeringChangeOrderDto.class
        );

        // 2. Submit ECO
        mockMvc.perform(put("/api/v1/manufacturing/ecos/" + ecoDto.getId() + "/submit?userId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("UNDER_REVIEW"));

        // 3. Approve ECO
        mockMvc.perform(put("/api/v1/manufacturing/ecos/" + ecoDto.getId() + "/approve?userId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("APPROVED"));

        // 4. Implement ECO
        mockMvc.perform(put("/api/v1/manufacturing/ecos/" + ecoDto.getId() + "/implement?userId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("IMPLEMENTED"));
    }

    @Test
    @WithMockUser(authorities = {"PRODUCTION_EXECUTE", "QUALITY_APPROVE", "MANUFACTURING_VIEW"})
    void testQualityInspectionWorkflow() throws Exception {
        // 1. Create Quality Inspection
        CreateQualityInspectionRequest qiReq = new CreateQualityInspectionRequest();
        qiReq.setCompanyId(testCompany.getId());
        qiReq.setInspectionNumber("QI-2026-0001");
        qiReq.setProductId(finishedGoods.getId());
        qiReq.setInspectionType("FINAL");
        qiReq.setSampleSize(new BigDecimal("10.0"));
        qiReq.setNotes("First inspection run");

        String qiStr = mockMvc.perform(post("/api/v1/manufacturing/quality/inspections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(qiReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andReturn().getResponse().getContentAsString();

        QualityInspectionDto qiDto = objectMapper.readValue(
                objectMapper.readTree(qiStr).get("data").toString(),
                QualityInspectionDto.class
        );

        // 2. Record Inspection Result (Passed = 10, Failed = 0)
        RecordInspectionResultRequest resReq = new RecordInspectionResultRequest();
        resReq.setPassedQuantity(new BigDecimal("10.0"));
        resReq.setFailedQuantity(BigDecimal.ZERO);
        resReq.setInspectorUserId(1L);
        resReq.setDisposition("ACCEPT");
        resReq.setNotes("Perfect lot");

        mockMvc.perform(put("/api/v1/manufacturing/quality/inspections/" + qiDto.getId() + "/record-result?companyId=" + testCompany.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.inspectedQuantity").value(10.0))
                .andExpect(jsonPath("$.data.holdProduction").value(false));

        // 3. Approve Quality Inspection
        mockMvc.perform(put("/api/v1/manufacturing/quality/inspections/" + qiDto.getId() + "/approve?companyId=" + testCompany.getId() + "&approvedBy=1&notes=Passed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("PASSED"));
    }

    @Test
    @WithMockUser(authorities = {"MANUFACTURING_ADMIN", "MANUFACTURING_VIEW"})
    void testManufacturingCalendarAndFiniteScheduling() throws Exception {
        // 1. Create Calendar configuration
        CreateCalendarRequest calReq = new CreateCalendarRequest();
        calReq.setCompanyId(testCompany.getId());
        calReq.setCode("CAL-WC-01");
        calReq.setName("Work Center Calendar 1");
        calReq.setCalendarType("WORK_CENTER");
        calReq.setReferenceType("WORK_CENTER");
        calReq.setReferenceId(101L);

        String calStr = mockMvc.perform(post("/api/v1/manufacturing/calendars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(calReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.code").value("CAL-WC-01"))
                .andReturn().getResponse().getContentAsString();

        CalendarDto calDto = objectMapper.readValue(
                objectMapper.readTree(calStr).get("data").toString(),
                CalendarDto.class
        );

        // 2. Add Shift to Calendar
        CreateShiftRequest shiftReq = new CreateShiftRequest();
        shiftReq.setShiftName("Day Shift");
        shiftReq.setStartTime(LocalTime.of(8, 0));
        shiftReq.setEndTime(LocalTime.of(17, 0));
        shiftReq.setBreakMinutes(60);
        shiftReq.setAvailableHours(new BigDecimal("8.00"));
        shiftReq.setDayOfWeek(1); // Monday

        mockMvc.perform(post("/api/v1/manufacturing/calendars/" + calDto.getId() + "/shifts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shiftReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.availableHours").value(8.00));

        // 3. Add Exception
        CreateExceptionRequest exReq = new CreateExceptionRequest();
        exReq.setExceptionDate(LocalDate.now().plusDays(2));
        exReq.setExceptionType("DOWNTIME");
        exReq.setAvailableHours(BigDecimal.ZERO);
        exReq.setDescription("Preventive Maintenance");

        mockMvc.perform(post("/api/v1/manufacturing/calendars/" + calDto.getId() + "/exceptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exReq)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "unauthorized_user", authorities = {"SOME_RANDOM_ROLE"})
    void testSecurityAccessDenied() throws Exception {
        // Attempt to create ECO without BOM_MANAGE/MANUFACTURING_ADMIN authority
        CreateEcoRequest ecoReq = new CreateEcoRequest();
        ecoReq.setCompanyId(testCompany.getId());
        ecoReq.setEcoNumber("ECO-DENIED-001");
        ecoReq.setTitle("ECO-DENIED");
        ecoReq.setRequestedBy(1L);

        mockMvc.perform(post("/api/v1/manufacturing/ecos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ecoReq)))
                .andExpect(status().isForbidden());
    }
}
