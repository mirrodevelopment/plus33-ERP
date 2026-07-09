/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.mdm
 * File              : MdmGoldenRecordService.java
 * Purpose           : Business logic service layer for Bi Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MdmGoldenRecordController
 * Related Service   : MdmGoldenRecordService
 * Related Repository: MdmGoldenRecordRepository
 * Related Entity    : MdmGoldenRecord
 * Related DTO       : MdmMergeRequest
 * Related Mapper    : MdmGoldenRecordMapper
 * Related DB Table  : mdm_golden_records
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MdmGoldenRecordController, MdmGoldenRecordServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Bi Module. Implements MdmGoldenRecordService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.bi.mdm;

import com.plus33.erp.bi.entity.*;
import com.plus33.erp.bi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code MdmGoldenRecordService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.mdm}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Bi Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * MdmGoldenRecordController
 *   --> MdmGoldenRecordService (this)
 *   --> Validate business rules
 *   --> MdmGoldenRecordRepository (read/write 'mdm_golden_records')
 *   --> MdmGoldenRecordMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code mdm_golden_records}</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class MdmGoldenRecordService {

    @Autowired MdmGoldenRecordRepository goldenRecordRepo;
    @Autowired MdmSourceMappingRepository sourceMappingRepo;
    @Autowired MdmDuplicateCandidateRepository duplicateRepo;
    @Autowired MdmMergeRequestRepository mergeRequestRepo;
    @Autowired MdmStewardWorkflowService stewardWorkflowService;
    @Autowired SurvivorshipEngine survivorshipEngine;
    @Autowired MdmMatchingService matchingService;
    /**
     * Creates a new golden record and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param recordType the recordType input value
     * @param displayName the displayName input value
     * @param email the email input value
     * @param phone the phone input value
     * @param address the address input value
     * @param taxNumber the taxNumber input value
     * @return the MdmGoldenRecord result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public MdmGoldenRecord createGoldenRecord(String recordType, String displayName, String email, String phone, String address, String taxNumber) {
        MdmGoldenRecord record = new MdmGoldenRecord();
        record.setRecordType(recordType);
        record.setDisplayName(displayName);
        record.setEmail(email);
        record.setPhone(phone);
        record.setAddress(address);
        record.setTaxNumber(taxNumber);
        record.setStatus("ACTIVE");
        record.setCreatedAt(LocalDateTime.now());
        record.setUpdatedAt(LocalDateTime.now());
        return goldenRecordRepo.save(record);
    }

    /**
     * Performs the evaluateIncomingRecord operation in this module.
     *
     * @param recordType the recordType input value
     * @param system the system input value
     * @param table the table input value
     * @param dimId the dimId input value
     * @param displayName the displayName input value
     * @param email the email input value
     * @param phone the phone input value
     * @param address the address input value
     * @param taxNumber the taxNumber input value
     */
    @Transactional
    public void evaluateIncomingRecord(String recordType, String system, String table, Long dimId, String displayName, String email, String phone, String address, String taxNumber) {
        if (displayName == null || displayName.trim().isEmpty()) {
            return;
        }

        List<MdmGoldenRecord> existing = goldenRecordRepo.findAll();
        MdmGoldenRecord matchedRecord = null;
        double bestScore = 0.0;

        for (MdmGoldenRecord gr : existing) {
            if (!gr.getRecordType().equalsIgnoreCase(recordType)) {
                continue;
            }
            double score = matchingService.calculateSimilarity(displayName, gr.getDisplayName());
            if (score > bestScore) {
                bestScore = score;
                matchedRecord = gr;
            }
        }

        if (bestScore >= 85.0 && matchedRecord != null) {
            createSourceMapping(matchedRecord.getId(), system, table, dimId, bestScore);
            recalculateAttributes(matchedRecord.getId());
        } else if (bestScore >= 60.0 && matchedRecord != null) {
            MdmDuplicateCandidate candidate = new MdmDuplicateCandidate();
            candidate.setRecordType(recordType);
            candidate.setSourceSystemA("MDM");
            candidate.setSourceTableA("bi_mdm_golden_record");
            candidate.setSourceDimIdA(matchedRecord.getId());
            candidate.setSourceSystemB(system);
            candidate.setSourceTableB(table);
            candidate.setSourceDimIdB(dimId);
            candidate.setSimilarityScore(java.math.BigDecimal.valueOf(bestScore));
            candidate.setStatus("PENDING");
            candidate.setDetectedAt(LocalDateTime.now());
            duplicateRepo.save(candidate);

            MdmMergeRequest request = new MdmMergeRequest();
            request.setRequestCode("REQ-" + System.currentTimeMillis() + "-" + dimId);
            request.setRecordType(recordType);
            request.setSourceSystemA("MDM");
            request.setSourceDimIdA(matchedRecord.getId());
            request.setSourceSystemB(system);
            request.setSourceDimIdB(dimId);
            request.setStatus("REQUESTED");
            request.setRequestedBy("SYSTEM");
            request.setComments("Auto-detected similarity score: " + bestScore);
            request.setCreatedAt(LocalDateTime.now());
            request.setUpdatedAt(LocalDateTime.now());
            mergeRequestRepo.save(request);
        } else {
            MdmGoldenRecord newGr = createGoldenRecord(recordType, displayName, email, phone, address, taxNumber);
            createSourceMapping(newGr.getId(), system, table, dimId, 100.0);
        }
    }

    private void createSourceMapping(Long goldenId, String system, String table, Long dimId, double score) {
        MdmGoldenRecord gr = goldenRecordRepo.findById(goldenId)
                .orElseThrow(() -> new IllegalArgumentException("Golden record not found"));
        MdmSourceMapping mapping = new MdmSourceMapping();
        mapping.setGoldenRecord(gr);
        mapping.setSourceSystem(system);
        mapping.setSourceTable(table);
        mapping.setSourceDimId(dimId);
        mapping.setConfidenceScore(java.math.BigDecimal.valueOf(score));
        mapping.setMappedAt(LocalDateTime.now());
        sourceMappingRepo.save(mapping);
    }

    /**
     * Performs the executeMerge operation in this module.
     *
     * @param mergeRequestId the mergeRequestId input value
     * @param stewardUser the stewardUser input value
     */
    @Transactional
    public void executeMerge(Long mergeRequestId, String stewardUser) {
        MdmMergeRequest request = mergeRequestRepo.findById(mergeRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Merge request not found: " + mergeRequestId));

        request.setStatus("MERGED");
        request.setUpdatedAt(LocalDateTime.now());
        mergeRequestRepo.save(request);

        Long goldenId = request.getSourceDimIdA();
        Long dimIdB = request.getSourceDimIdB();
        MdmGoldenRecord grA = goldenRecordRepo.findById(goldenId)
                .orElseThrow(() -> new IllegalArgumentException("Golden record not found"));
        
        List<MdmSourceMapping> mappings = sourceMappingRepo.findAll();
        for (MdmSourceMapping m : mappings) {
            if (m.getSourceDimId().equals(dimIdB)) {
                m.setGoldenRecord(grA);
                sourceMappingRepo.save(m);
            }
        }

        recalculateAttributes(goldenId);
        
        request.setStatus("COMPLETED");
        mergeRequestRepo.save(request);
    }

    private void recalculateAttributes(Long goldenId) {
        MdmGoldenRecord gr = goldenRecordRepo.findById(goldenId).orElse(null);
        if (gr == null) return;

        List<MdmSourceMapping> mappings = sourceMappingRepo.findAll();
        List<SurvivorshipEngine.AttributeCandidate> nameCandidates = new ArrayList<>();
        List<SurvivorshipEngine.AttributeCandidate> emailCandidates = new ArrayList<>();
        List<SurvivorshipEngine.AttributeCandidate> phoneCandidates = new ArrayList<>();
        List<SurvivorshipEngine.AttributeCandidate> addressCandidates = new ArrayList<>();
        List<SurvivorshipEngine.AttributeCandidate> taxCandidates = new ArrayList<>();

        for (MdmSourceMapping m : mappings) {
            if (!m.getGoldenRecord().getId().equals(goldenId)) {
                continue;
            }
            int priority = 3;
            if ("CRM".equalsIgnoreCase(m.getSourceSystem())) priority = 1;
            else if ("WMS".equalsIgnoreCase(m.getSourceSystem())) priority = 2;

            LocalDateTime now = LocalDateTime.now();
            nameCandidates.add(new SurvivorshipEngine.AttributeCandidate(gr.getDisplayName(), m.getConfidenceScore().doubleValue(), now, now, priority));
            emailCandidates.add(new SurvivorshipEngine.AttributeCandidate(gr.getEmail(), m.getConfidenceScore().doubleValue(), now, now, priority));
            phoneCandidates.add(new SurvivorshipEngine.AttributeCandidate(gr.getPhone(), m.getConfidenceScore().doubleValue(), now, now, priority));
            addressCandidates.add(new SurvivorshipEngine.AttributeCandidate(gr.getAddress(), m.getConfidenceScore().doubleValue(), now, now, priority));
            taxCandidates.add(new SurvivorshipEngine.AttributeCandidate(gr.getTaxNumber(), m.getConfidenceScore().doubleValue(), now, now, priority));
        }

        String resolvedName = survivorshipEngine.resolveAttribute("displayName", nameCandidates, AttributeResolutionRule.TRUSTED_SOURCE_PRIORITY, null);
        String resolvedEmail = survivorshipEngine.resolveAttribute("email", emailCandidates, AttributeResolutionRule.MOST_RECENT, null);
        String resolvedPhone = survivorshipEngine.resolveAttribute("phone", phoneCandidates, AttributeResolutionRule.HIGHEST_CONFIDENCE, null);
        String resolvedAddress = survivorshipEngine.resolveAttribute("address", addressCandidates, AttributeResolutionRule.LONGEST_HISTORY, null);
        String resolvedTax = survivorshipEngine.resolveAttribute("taxNumber", taxCandidates, AttributeResolutionRule.TRUSTED_SOURCE_PRIORITY, null);

        if (resolvedName != null) gr.setDisplayName(resolvedName);
        if (resolvedEmail != null) gr.setEmail(resolvedEmail);
        if (resolvedPhone != null) gr.setPhone(resolvedPhone);
        if (resolvedAddress != null) gr.setAddress(resolvedAddress);
        if (resolvedTax != null) gr.setTaxNumber(resolvedTax);

        gr.setUpdatedAt(LocalDateTime.now());
        goldenRecordRepo.save(gr);
    }
}