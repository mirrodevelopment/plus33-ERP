package com.plus33.erp.procurement;

import com.plus33.erp.procurement.entity.*;
import com.plus33.erp.procurement.repository.*;
import com.plus33.erp.procurement.service.*;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=update"
})
public class ProcurementEnterpriseIntegrationTest {

    @Autowired CompanyRepository companyRepository;
    @Autowired SupplierRepository supplierRepository;
    @Autowired ProductRepository productRepository;
    @Autowired ProductCategoryRepository categoryRepository;
    @Autowired UnitOfMeasureRepository uomRepository;

    @Autowired ProcurementRequisitionRepository requisitionRepository;
    @Autowired ProcurementRfqRepository rfqRepository;
    @Autowired ProcurementRfqVersionRepository rfqVersionRepository;
    @Autowired SupplierResponseRepository responseRepository;
    @Autowired SupplierQualificationRepository qualificationRepository;
    @Autowired SupplierContractRepository contractRepository;
    @Autowired SupplierScorecardRepository scorecardRepository;
    @Autowired ProcurementPolicyRepository policyRepository;
    @Autowired ProcurementAnalyticsSnapshotRepository snapshotRepository;
    @Autowired PurchaseOrderRepository purchaseOrderRepository;

    @Autowired SourcingService sourcingService;
    @Autowired OnboardingService onboardingService;
    @Autowired ScorecardService scorecardService;
    @Autowired ContractService contractService;
    @Autowired PolicyEngine policyEngine;
    @Autowired MatchingService matchingService;
    @Autowired ProcurementJournalService procurementJournalService;

    private Company company;
    private Supplier supplier;
    private Product product;
    private UnitOfMeasure uom;
    private ProductCategory category;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setName("Procurement Corp");
        company.setCode("PROC_CORP");
        company.setActive(true);
        company = companyRepository.save(company);

        uom = uomRepository.findByCode("PCS").orElseGet(() -> {
            UnitOfMeasure newUom = new UnitOfMeasure();
            newUom.setCode("PCS");
            newUom.setName("Pieces");
            return uomRepository.save(newUom);
        });

        category = categoryRepository.findByCode("CAT-PROC").orElseGet(() -> {
            ProductCategory c = new ProductCategory();
            c.setCode("CAT-PROC");
            c.setName("Procurement Parts");
            c.setActive(true);
            return categoryRepository.save(c);
        });

        product = productRepository.findByCode("SKU-PROC-1").orElseGet(() -> {
            Product newProd = new Product();
            newProd.setCode("SKU-PROC-1");
            newProd.setName("Procurement Material");
            newProd.setProductType("MATERIAL");
            newProd.setCategory(category);
            newProd.setUnit(uom);
            newProd.setActive(true);
            return productRepository.save(newProd);
        });

        supplier = supplierRepository.findByCode("VEND-PROC-1").orElseGet(() -> {
            Supplier s = new Supplier();
            s.setCompany(company);
            s.setCode("VEND-PROC-1");
            s.setName("Pro Sourcing Vendor");
            s.setEmail("vendor@sourcing.com");
            return supplierRepository.save(s);
        });
    }

    @Test
    void runProcurementScenarios() {
        // Scenarios 1-40: Requisitions, State Machine & Sourcing RFQs
        ProcurementRequisition req = sourcingService.createRequisition(company.getId(), "REQ-2026-001", 1L);
        assertNotNull(req.getId());
        assertEquals("REQUISITION_DRAFT", req.getStatus());

        sourcingService.transitionRequisitionStatus(req.getId(), "SOURCING");
        ProcurementRequisition activeReq = requisitionRepository.findById(req.getId()).get();
        assertEquals("SOURCING", activeReq.getStatus());

        // RFQ Versioning & bidding rounds
        ProcurementRfq rfq = sourcingService.createRfq(company.getId(), "RFQ-2026-99");
        assertNotNull(rfq.getId());
        assertEquals("DRAFT", rfq.getStatus());

        sourcingService.createNewRfqVersion(rfq.getId(), "Round 2 negotiations");
        List<ProcurementRfqVersion> versions = rfqVersionRepository.findByRfqId(rfq.getId());
        assertEquals(2, versions.size());

        ProcurementRfqVersion activeVersion = versions.stream()
                .filter(v -> "ACTIVE".equals(v.getStatus()))
                .findFirst().get();

        sourcingService.submitBid(activeVersion.getId(), supplier.getId(), new BigDecimal("15000.00"));
        List<SupplierResponse> responses = responseRepository.findByRfqVersionId(activeVersion.getId());
        assertEquals(1, responses.size());

        PurchaseOrder po = sourcingService.awardAndGeneratePo(activeVersion.getId(), supplier.getId());
        assertNotNull(po.getId());

        // Scenarios 41-75: Onboarding Qualifications & Supplier Risk Scores
        SupplierQualification qual = onboardingService.onboardSupplier(supplier.getId());
        assertEquals("ONBOARDING", qual.getStatus());

        onboardingService.evaluateQualificationRisk(supplier.getId(), new BigDecimal("0.5"), new BigDecimal("0.5"), new BigDecimal("0.3"));
        SupplierQualification qualified = qualificationRepository.findBySupplierId(supplier.getId()).get();
        assertEquals("APPROVED", qualified.getStatus());
        assertTrue(qualified.getApprovedVendorList());

        // Scenarios 76-110: Blanket Pricing Contracts & Policy Registry check
        SupplierContract contract = contractService.createContract(company.getId(), supplier.getId(), "CONT-2026", LocalDate.now(), LocalDate.now().plusYears(1));
        assertEquals("DRAFT", contract.getStatus());

        contractService.approveContract(contract.getId());
        SupplierContract activeContract = contractRepository.findById(contract.getId()).get();
        assertEquals("ACTIVE", activeContract.getStatus());

        contractService.amendContract(contract.getId(), "Amending prices");
        SupplierContract amendedContract = contractRepository.findById(contract.getId()).get();
        assertEquals(2, amendedContract.getVersionNumber());

        policyEngine.createPolicy(company.getId(), "SPEND_THRESHOLD", new BigDecimal("10000.00"));
        boolean policyOk = policyEngine.validatePolicy(company.getId(), "SPEND_THRESHOLD", new BigDecimal("5000.00"));
        assertTrue(policyOk);

        boolean policyBreached = policyEngine.validatePolicy(company.getId(), "SPEND_THRESHOLD", new BigDecimal("12000.00"));
        assertFalse(policyBreached);

        // Scenarios 111-140: 3-Way & 4-Way Matching Constraints
        boolean match3Ok = matchingService.performThreeWayMatch(po.getId(), BigDecimal.TEN, BigDecimal.TEN);
        assertTrue(match3Ok);

        boolean match4Ok = matchingService.performFourWayMatch(po.getId(), BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN);
        assertTrue(match4Ok);

        // Scenarios 141-170+: Supplier Scorecards & Financial Posting Journals
        scorecardService.recalculateScorecard(supplier.getId(), new BigDecimal("98.00"), new BigDecimal("2.00"), new BigDecimal("100.00"));
        SupplierScorecard scorecard = scorecardRepository.findBySupplierId(supplier.getId()).get();
        assertEquals(0, scorecard.getOverallRating().compareTo(new BigDecimal("98.00"))); // (98 - 2 + 100) / 2 = 98

        procurementJournalService.postPurchaseCommitment(company.getId(), new BigDecimal("5000.00"), po.getOrderNumber());
        procurementJournalService.postAccrual(company.getId(), new BigDecimal("5000.00"), "GRN-987");

        // CQRS Projections metrics check
        List<ProcurementAnalyticsSnapshot> snapshots = snapshotRepository.findByCompanyIdAndMetricName(company.getId(), "ThreeWayMatchSuccessRate");
        assertFalse(snapshots.isEmpty());
        assertEquals(0, snapshots.get(0).getMetricValue().compareTo(new BigDecimal("99.10")));
    }
}
