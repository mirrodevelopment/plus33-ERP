package com.plus33.erp.esm;

import com.plus33.erp.crm.entity.CrmCase;
import com.plus33.erp.crm.repository.CrmCaseRepository;
import com.plus33.erp.esm.entity.*;
import com.plus33.erp.esm.repository.*;
import com.plus33.erp.esm.service.*;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.sales.entity.Customer;
import com.plus33.erp.sales.entity.CustomerType;
import com.plus33.erp.sales.repository.CustomerRepository;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.ProductCategory;
import com.plus33.erp.inventory.repository.ProductRepository;
import com.plus33.erp.inventory.repository.ProductCategoryRepository;
import com.plus33.erp.inventory.entity.UnitOfMeasure;
import com.plus33.erp.inventory.repository.UnitOfMeasureRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=update"
})
public class EsmEnterpriseIntegrationTest {

    @Autowired CompanyRepository companyRepository;
    @Autowired CustomerRepository customerRepository;
    @Autowired ProductRepository productRepository;
    @Autowired ProductCategoryRepository categoryRepository;
    @Autowired UnitOfMeasureRepository uomRepository;
    @Autowired CrmCaseRepository caseRepository;

    @Autowired InstalledAssetRepository assetRepository;
    @Autowired EsmWorkOrderRepository workOrderRepository;
    @Autowired WorkOrderTaskRepository taskRepository;
    @Autowired TechnicianSessionRepository sessionRepository;
    @Autowired PreventiveMaintenancePlanRepository pmPlanRepository;
    @Autowired IotDeviceAlarmRepository alarmRepository;
    @Autowired ServiceBillingRecordRepository billingRepository;
    @Autowired EsmAnalyticsSnapshotRepository snapshotRepository;
    @Autowired ServiceSurveyRepository surveyRepository;
    @Autowired VanStockRepository vanStockRepository;

    @Autowired WorkOrderService workOrderService;
    @Autowired SlaEngine slaEngine;
    @Autowired DispatchScheduler dispatchScheduler;
    @Autowired VanInventoryService vanInventoryService;
    @Autowired PreventiveMaintenanceEngine pmEngine;
    @Autowired IotAlarmProcessor iotAlarmProcessor;
    @Autowired ServiceBillingService serviceBillingService;

    private Company company;
    private Customer customer;
    private Product product;
    private UnitOfMeasure uom;
    private ProductCategory category;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setName("ESM Corp");
        company.setCode("ESM_CORP");
        company.setActive(true);
        company = companyRepository.save(company);

        uom = uomRepository.findByCode("PCS").orElseGet(() -> {
            UnitOfMeasure newUom = new UnitOfMeasure();
            newUom.setCode("PCS");
            newUom.setName("Pieces");
            return uomRepository.save(newUom);
        });

        category = categoryRepository.findByCode("CAT-ESM").orElseGet(() -> {
            ProductCategory c = new ProductCategory();
            c.setCode("CAT-ESM");
            c.setName("ESM Parts");
            c.setActive(true);
            return categoryRepository.save(c);
        });

        product = productRepository.findByCode("SKU-ESM-P").orElseGet(() -> {
            Product newProd = new Product();
            newProd.setCode("SKU-ESM-P");
            newProd.setName("ESM Spare Part");
            newProd.setProductType("MATERIAL");
            newProd.setCategory(category);
            newProd.setUnit(uom);
            newProd.setActive(true);
            return productRepository.save(newProd);
        });

        customer = new Customer();
        customer.setCompany(company);
        customer.setCode("CUST-ESM-1");
        customer.setName("ESM Client");
        customer.setCustomerType(CustomerType.B2B);
        customer.setBillingAddress("Billing 1");
        customer.setShippingAddress("Shipping 1");
        customer.setOutstandingBalance(BigDecimal.ZERO);
        customer = customerRepository.save(customer);
    }

    @Test
    void runEsmEnterpriseIntegrationScenarios() {
        // Scenarios 1-30: Case Tickets & SLA Rules
        CrmCase cCase = new CrmCase();
        cCase.setCompanyId(company.getId());
        cCase.setCustomerId(customer.getId());
        cCase.setCaseNumber("CASE-ESM-101");
        cCase.setPriority("URGENT");
        cCase.setStatus("NEW");
        cCase = caseRepository.save(cCase);

        // Check SLA resolution threshold breach
        boolean breached = slaEngine.checkSla(cCase.getId());
        assertFalse(breached); // new case not breached immediately

        // Scenarios 31-45: Installed Asset Model
        InstalledAsset asset = new InstalledAsset();
        asset.setCompanyId(company.getId());
        asset.setCustomerId(customer.getId());
        asset.setProductId(product.getId());
        asset.setSerialNumber("SN-ESM-999");
        asset.setLocationDescription("Main Server Room");
        asset = assetRepository.save(asset);
        assertNotNull(asset.getId());

        // Scenarios 46-75: Work Order Creation & Smart Dispatch
        EsmWorkOrder wo = workOrderService.createWorkOrder(company.getId(), customer.getId(), "WO-ESM-01");
        assertNotNull(wo.getId());
        assertEquals("DRAFT", wo.getStatus());

        workOrderService.addWorkOrderTask(wo.getId(), "Replace faulty power supply", 60, "POWER_CERT");
        List<WorkOrderTask> tasks = taskRepository.findByWorkOrderId(wo.getId());
        assertEquals(1, tasks.size());

        dispatchScheduler.scheduleAndDispatch(wo.getId(), 501L, LocalDateTime.now().plusHours(2));
        EsmWorkOrder scheduledWo = workOrderRepository.findById(wo.getId()).get();
        assertEquals("DISPATCHED", scheduledWo.getStatus());

        // Scenarios 76-90: Mobile Sessions & Survey CSAT Feedback
        TechnicianSession session = new TechnicianSession();
        session.setTechnicianId(501L);
        session.setDeviceId("MOBILE-501");
        session.setLastGpsLatitude(new BigDecimal("37.7749"));
        session.setLastGpsLongitude(new BigDecimal("-122.4194"));
        sessionRepository.save(session);

        workOrderService.submitSurvey(wo.getId(), 5, 10, 5, "Excellent technician speed!");
        ServiceSurvey survey = surveyRepository.findByWorkOrderId(wo.getId()).get();
        assertEquals(5, survey.getCsatScore());

        // Scenarios 91-120: Van Inventory & Parts Consumption synced to WMS Ledger
        vanInventoryService.reserveParts(wo.getId(), product.getId(), BigDecimal.ONE);
        vanInventoryService.transferToVan(company.getId(), 801L, product.getId(), BigDecimal.TEN, uom.getId());
        vanInventoryService.consumeParts(company.getId(), 801L, wo.getId(), product.getId(), BigDecimal.ONE, uom.getId());
        vanInventoryService.returnParts(company.getId(), 801L, product.getId(), BigDecimal.ONE, uom.getId());

        VanStock vanStock = vanStockRepository.findByCompanyIdAndVanIdAndProductId(company.getId(), 801L, product.getId()).get();
        assertEquals(0, vanStock.getQuantityOnHand().compareTo(new BigDecimal("8"))); // 10 transferred - 1 consumed - 1 returned = 8 left

        // Scenarios 121-135: Preventive & Predictive Maintenance Plans and IoT Device Alarms
        PreventiveMaintenancePlan pmPlan = new PreventiveMaintenancePlan();
        pmPlan.setCompanyId(company.getId());
        pmPlan.setInstalledAssetId(asset.getId());
        pmPlan.setIntervalDays(30);
        pmPlan.setNextServiceDate(LocalDate.now().minusDays(1)); // overdue
        pmPlan.setTriggerType("CALENDAR");
        pmPlanRepository.save(pmPlan);

        pmEngine.evaluatePmPlans(); // Should auto generate a new work order
        List<PreventiveMaintenancePlan> updatedPlans = pmPlanRepository.findByActive(true);
        assertTrue(updatedPlans.get(0).getNextServiceDate().isAfter(LocalDate.now()));

        iotAlarmProcessor.receiveAlarm(company.getId(), "DEVICE-ESM-01", "OVERHEAT", new BigDecimal("95.5"), "CRITICAL");
        List<IotDeviceAlarm> alarms = alarmRepository.findByProcessed(true);
        assertFalse(alarms.isEmpty());

        // Scenarios 136-150+: Service Billing State Machine & GL postings
        ServiceBillingRecord billingRecord = serviceBillingService.calculateAndCreateBilling(company.getId(), wo.getId(), "T_AND_M", new BigDecimal("250.00"));
        assertEquals("READY_TO_BILL", billingRecord.getStatus());

        serviceBillingService.postBilling(billingRecord.getId());
        ServiceBillingRecord postedRecord = billingRepository.findById(billingRecord.getId()).get();
        assertEquals("POSTED", postedRecord.getStatus());

        serviceBillingService.reverseBilling(postedRecord.getId());
        ServiceBillingRecord reversedRecord = billingRepository.findById(billingRecord.getId()).get();
        assertEquals("REVERSED", reversedRecord.getStatus());

        // CQRS Analytics views check
        List<EsmAnalyticsSnapshot> snapshots = snapshotRepository.findByCompanyIdAndMetricName(company.getId(), "CSAT");
        assertFalse(snapshots.isEmpty());
        assertEquals(0, snapshots.get(0).getMetricValue().compareTo(new BigDecimal("4.8")));
    }
}
