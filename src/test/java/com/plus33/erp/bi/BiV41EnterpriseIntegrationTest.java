package com.plus33.erp.bi;

import com.plus33.erp.bi.entity.*;
import com.plus33.erp.bi.mdm.*;
import com.plus33.erp.bi.governance.*;
import com.plus33.erp.bi.insights.*;
import com.plus33.erp.bi.contract.*;
import com.plus33.erp.bi.selfservice.*;
import com.plus33.erp.bi.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BiV41EnterpriseIntegrationTest {

    @Autowired MdmGoldenRecordService goldenRecordService;
    @Autowired MdmStewardWorkflowService stewardWorkflowService;
    @Autowired MdmMatchingService matchingService;
    @Autowired DataMaskingService maskingService;
    @Autowired DataCatalogService catalogService;
    @Autowired AiInsightsService insightsService;
    @Autowired DataContractValidator contractValidator;
    @Autowired SchemaEvolutionTracker evolutionTracker;
    @Autowired SelfServiceAnalyticsService selfService;
    @Autowired DashboardSharingService sharingService;

    @Autowired MdmGoldenRecordRepository goldenRecordRepo;
    @Autowired MdmSourceMappingRepository sourceMappingRepo;
    @Autowired MdmDuplicateCandidateRepository duplicateRepo;
    @Autowired MdmMergeRequestRepository mergeRequestRepo;
    @Autowired MdmStewardAssignmentRepository assignmentRepo;
    @Autowired MdmStewardDecisionRepository decisionRepo;
    @Autowired BiCatalogDatasetRepository datasetRepo;
    @Autowired BiCatalogGlossaryRepository glossaryRepo;
    @Autowired BiGovernanceClassificationRepository classificationRepo;
    @Autowired BiGovernanceMaskingRuleRepository maskingRuleRepo;
    @Autowired BiAiInsightGenerationRepository insightRepo;
    @Autowired BiDataContractRepository contractRepo;
    @Autowired BiSchemaEvolutionHistoryRepository evolutionRepo;
    @Autowired BiSelfServiceWorkspaceRepository workspaceRepo;
    @Autowired BiDashboardShareRepository shareRepo;
    @Autowired BiDashboardSubscriptionRepository subscriptionRepo;

    @Autowired org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    private static boolean dbCleaned = false;

    @BeforeEach
    void cleanDbOnce() {
        if (!dbCleaned) {
            jdbcTemplate.execute("TRUNCATE TABLE bi_mdm_steward_decision, bi_mdm_steward_assignment, bi_mdm_merge_request, bi_mdm_duplicate_candidate, bi_mdm_source_mapping, bi_mdm_golden_record CASCADE");
            jdbcTemplate.execute("TRUNCATE TABLE bi_catalog_dataset, bi_catalog_glossary CASCADE");
            jdbcTemplate.execute("TRUNCATE TABLE bi_governance_masking_rule, bi_governance_classification CASCADE");
            jdbcTemplate.execute("TRUNCATE TABLE bi_ai_insight_generation CASCADE");
            jdbcTemplate.execute("TRUNCATE TABLE bi_data_contract CASCADE");
            jdbcTemplate.execute("TRUNCATE TABLE bi_schema_evolution_history CASCADE");
            jdbcTemplate.execute("TRUNCATE TABLE bi_self_service_workspace CASCADE");
            jdbcTemplate.execute("TRUNCATE TABLE bi_dashboard_share, bi_dashboard_subscription CASCADE");
            dbCleaned = true;
        }
    }

    @Test @Order(1)
    void calculateSimilarity_evaluatesAccurately() {
        double exact = matchingService.calculateSimilarity("Acme Corp", "Acme Corp");
        double close = matchingService.calculateSimilarity("Acme Corp Inc", "Acme Corp");
        double diff = matchingService.calculateSimilarity("Acme Corp", "Beta LLC");
        assertEquals(100.0, exact);
        assertTrue(close > 60.0);
        assertTrue(diff < 30.0);
    }

    @Test @Order(10)
    void evaluateIncomingRecord_createsGoldenRecordIfUnique() {
        goldenRecordService.evaluateIncomingRecord("CUSTOMER", "CRM", "dim_customer", 101L, "Apex Solutions", "apex@example.com", "111-222", "123 St", "TX-111");
        List<MdmGoldenRecord> goldens = goldenRecordRepo.findAll();
        assertFalse(goldens.isEmpty());
        assertTrue(goldens.stream().anyMatch(g -> g.getDisplayName().equalsIgnoreCase("Apex Solutions")));
    }

    @Test @Order(20)
    void evaluateIncomingRecord_flagsDuplicateCandidateIfModerate() {
        goldenRecordService.evaluateIncomingRecord("CUSTOMER", "CRM", "dim_customer", 102L, "Apex Soluti", "apex@example.com", "111-222", "123 St", "TX-111");
        List<MdmDuplicateCandidate> candidates = duplicateRepo.findAll();
        assertFalse(candidates.isEmpty());
    }

    @Test @Order(30)
    void workflowStewardAssignment_transitionsMergeRequestState() {
        MdmMergeRequest mr = mergeRequestRepo.findAll().stream().findFirst().orElse(null);
        assertNotNull(mr);
        assertEquals("REQUESTED", mr.getStatus());

        MdmStewardAssignment assignment = stewardWorkflowService.createAssignment(mr.getId(), "steward_1");
        assertEquals("ASSIGNED", assignment.getStatus());

        MdmMergeRequest updatedMr = mergeRequestRepo.findById(mr.getId()).orElse(null);
        assertNotNull(updatedMr);
        assertEquals("UNDER_REVIEW", updatedMr.getStatus());

        MdmStewardDecision decision = stewardWorkflowService.resolveDecision(assignment.getId(), "APPROVE", "Looks like a match");
        assertEquals("APPROVE", decision.getDecision());

        MdmMergeRequest finalMr = mergeRequestRepo.findById(mr.getId()).orElse(null);
        assertNotNull(finalMr);
        assertEquals("APPROVED", finalMr.getStatus());
    }

    @Test @Order(40)
    void executeMerge_completesMergeAndRepointsSource() {
        MdmMergeRequest mr = mergeRequestRepo.findAll().stream()
                .filter(m -> "APPROVED".equalsIgnoreCase(m.getStatus()))
                .findFirst().orElse(null);
        assertNotNull(mr);

        goldenRecordService.executeMerge(mr.getId(), "steward_1");
        
        MdmMergeRequest merged = mergeRequestRepo.findById(mr.getId()).orElse(null);
        assertNotNull(merged);
        assertEquals("COMPLETED", merged.getStatus());
    }

    @Test @Order(50)
    void survivorshipEngine_resolvesPriorityCorrectly() {
        SurvivorshipEngine engine = new SurvivorshipEngine();
        List<SurvivorshipEngine.AttributeCandidate> list = List.of(
            new SurvivorshipEngine.AttributeCandidate("CRM Name", 90.0, LocalDateTime.now(), LocalDateTime.now(), 1),
            new SurvivorshipEngine.AttributeCandidate("ERP Name", 95.0, LocalDateTime.now(), LocalDateTime.now(), 3)
        );
        String resolved = engine.resolveAttribute("displayName", list, AttributeResolutionRule.TRUSTED_SOURCE_PRIORITY, null);
        assertEquals("CRM Name", resolved);
    }

    @Test @Order(60)
    void survivorshipEngine_resolvesMostRecent() {
        SurvivorshipEngine engine = new SurvivorshipEngine();
        LocalDateTime oldTime = LocalDateTime.now().minusDays(1);
        LocalDateTime newTime = LocalDateTime.now();
        List<SurvivorshipEngine.AttributeCandidate> list = List.of(
            new SurvivorshipEngine.AttributeCandidate("Old Name", 99.0, oldTime, oldTime, 1),
            new SurvivorshipEngine.AttributeCandidate("New Name", 90.0, newTime, newTime, 2)
        );
        String resolved = engine.resolveAttribute("displayName", list, AttributeResolutionRule.MOST_RECENT, null);
        assertEquals("New Name", resolved);
    }

    @Test @Order(70)
    void survivorshipEngine_resolvesManualOverride() {
        SurvivorshipEngine engine = new SurvivorshipEngine();
        List<SurvivorshipEngine.AttributeCandidate> list = List.of(
            new SurvivorshipEngine.AttributeCandidate("CRM Name", 99.0, LocalDateTime.now(), LocalDateTime.now(), 1)
        );
        String resolved = engine.resolveAttribute("displayName", list, AttributeResolutionRule.MANUAL_OVERRIDE, "Steward Override Name");
        assertEquals("Steward Override Name", resolved);
    }

    @Test @Order(80)
    void registerAndCertifyDataset_savesCorrectMetadata() {
        BiCatalogDataset ds = catalogService.registerDataset("fact_finance", "Finance Fact table", "ROLE_FINANCE_HEAD", "steward_1");
        assertNotNull(ds.getId());
        assertEquals("BRONZE", ds.getCertificationStatus());

        BiCatalogDataset certified = catalogService.certifyDataset(ds.getId(), "GOLD");
        assertEquals("GOLD", certified.getCertificationStatus());
    }

    @Test @Order(90)
    void createGlossaryTerm_persistsTerm() {
        BiCatalogGlossary term = catalogService.createGlossaryTerm("NET_REVENUE", "Net Revenue", "Gross sales minus discounts and returns", "SUM(amount) - SUM(discounts)", "FINANCE");
        assertNotNull(term.getId());
        assertEquals("NET_REVENUE", term.getTermCode());
    }

    @Test @Order(110)
    void dataMasking_redactsFullCorrectly() {
        BiGovernanceClassification c = new BiGovernanceClassification();
        c.setTableName("dim_employee");
        c.setColumnName("ssn");
        c.setClassificationLevel("CONFIDENTIAL");
        classificationRepo.save(c);

        BiGovernanceMaskingRule rule = new BiGovernanceMaskingRule();
        rule.setRuleName("SSN_Rule");
        rule.setClassificationLevel("CONFIDENTIAL");
        rule.setMaskingType("REDACT_FULL");
        maskingRuleRepo.save(rule);

        String masked = maskingService.maskValue("dim_employee", "ssn", "123-456-789");
        assertEquals("[REDACTED]", masked);
    }

    @Test @Order(120)
    void dataMasking_redactsPartialCorrectly() {
        BiGovernanceClassification c = new BiGovernanceClassification();
        c.setTableName("dim_customer");
        c.setColumnName("email");
        c.setClassificationLevel("PII");
        classificationRepo.save(c);

        BiGovernanceMaskingRule rule = new BiGovernanceMaskingRule();
        rule.setRuleName("Email_Rule");
        rule.setClassificationLevel("PII");
        rule.setMaskingType("REDACT_PARTIAL");
        maskingRuleRepo.save(rule);

        String masked = maskingService.maskValue("dim_customer", "email", "john.doe@example.com");
        assertEquals("jo****om", masked);
    }

    @Test @Order(130)
    void dataMasking_hashesCorrectly() {
        BiGovernanceClassification c = new BiGovernanceClassification();
        c.setTableName("dim_customer");
        c.setColumnName("tax_no");
        c.setClassificationLevel("PCI");
        classificationRepo.save(c);

        BiGovernanceMaskingRule rule = new BiGovernanceMaskingRule();
        rule.setRuleName("Tax_Rule");
        rule.setClassificationLevel("PCI");
        rule.setMaskingType("HASH_SHA256");
        maskingRuleRepo.save(rule);

        String masked = maskingService.maskValue("dim_customer", "tax_no", "SECRET-TAX");
        assertNotEquals("SECRET-TAX", masked);
        assertEquals(64, masked.length());
    }

    @Test @Order(140)
    void generateInsight_routesToCorrectProviderAndSavesLog() {
        BiAiInsightGeneration gen = insightsService.generateKpiInsight(1L, "REV_KPI", "FINANCE", BigDecimal.valueOf(12.50));
        assertNotNull(gen.getId());
        assertTrue(gen.getNarrativeText().contains("accrual matching rules"));

        BiAiInsightGeneration genSales = insightsService.generateKpiInsight(1L, "SALES_KPI", "SALES", BigDecimal.valueOf(-3.40));
        assertTrue(genSales.getNarrativeText().contains("distributor incentives"));
    }

    @Test @Order(150)
    void recordFeedback_updatesStewardFeedback() {
        BiAiInsightGeneration gen = insightRepo.findAll().stream().findFirst().orElse(null);
        assertNotNull(gen);

        BiAiInsightGeneration updated = insightsService.recordFeedback(gen.getId(), 5, true);
        assertEquals(5, updated.getFeedbackRating());
        assertTrue(updated.getAcceptedByUser());
    }

    @Test @Order(170)
    void dataContracts_validatesBackwardCompatibilitySuccessfully() {
        BiDataContract c = new BiDataContract();
        c.setContractName("stg_finance");
        c.setSchemaDefinition("id:long,amount:double");
        c.setCompatibilityLevel("BACKWARD");
        c.setStatus("ACTIVE");
        c.setCreatedBy("admin");
        contractRepo.save(c);

        List<DataContractValidator.ColumnMetadata> cols = List.of(
            new DataContractValidator.ColumnMetadata("id", "long"),
            new DataContractValidator.ColumnMetadata("amount", "double"),
            new DataContractValidator.ColumnMetadata("new_col", "varchar")
        );
        boolean valid = contractValidator.validateContract("stg_finance", cols);
        assertTrue(valid);

        List<DataContractValidator.ColumnMetadata> colsDeleted = List.of(
            new DataContractValidator.ColumnMetadata("id", "long")
        );
        boolean invalid = contractValidator.validateContract("stg_finance", colsDeleted);
        assertFalse(invalid);

        List<BiSchemaEvolutionHistory> history = evolutionRepo.findAll();
        assertTrue(history.stream().anyMatch(h -> h.getTableName().equalsIgnoreCase("stg_finance") && "REJECTED".equalsIgnoreCase(h.getValidationStatus())));
    }

    @Test @Order(190)
    void executeQuery_enforcesReadonlyWhitelistsAndTenantIsolation() {
        assertThrows(SecurityException.class, () -> {
            selfService.executeQuery("user_1", 1L, "DELETE FROM fact_finance");
        });

        assertThrows(SecurityException.class, () -> {
            selfService.executeQuery("user_1", 1L, "SELECT * FROM secret_system_table WHERE company_id = 1");
        });

        assertThrows(SecurityException.class, () -> {
            selfService.executeQuery("user_1", 1L, "SELECT * FROM fact_finance WHERE company_id = 2");
        });

        SelfServiceAnalyticsService.QueryResult res = selfService.executeQuery("user_1", 1L, "SELECT * FROM fact_finance WHERE company_id = 1");
        assertEquals("SUCCESS", res.getStatus());
    }

    @Test @Order(220)
    void shareAndSubscribe_persistsPermissionsAndAlerts() {
        BiDashboardShare share = sharingService.shareDashboard("DASH-01", "admin", "user_2", true);
        assertNotNull(share.getId());
        assertTrue(share.getCanEdit());

        BiDashboardSubscription sub = sharingService.subscribeDashboard("DASH-01", "user_2", "0 0 12 * * ?", "PDF");
        assertNotNull(sub.getId());
        assertEquals("ACTIVE", sub.getStatus());
    }

    @Test @Order(250)
    void testExecution_assertsV41ReleaseCompleteness() {
        assertTrue(goldenRecordRepo.count() > 0);
        assertTrue(glossaryRepo.count() > 0);
        assertTrue(maskingRuleRepo.count() > 0);
        assertTrue(insightRepo.count() > 0);
        assertTrue(contractRepo.count() > 0);
        
        selfService.saveWorkspace("WORK-01", "My Workspace", "user_1", 1L, "{}");
        assertTrue(workspaceRepo.count() > 0);
    }
}