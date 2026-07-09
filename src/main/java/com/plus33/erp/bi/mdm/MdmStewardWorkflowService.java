/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.mdm
 * File              : MdmStewardWorkflowService.java
 * Purpose           : Business logic service layer for Bi Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MdmStewardWorkflowController
 * Related Service   : MdmStewardWorkflowService
 * Related Repository: MdmStewardWorkflowRepository
 * Related Entity    : MdmStewardWorkflow
 * Related DTO       : getMergeRequest, MdmMergeRequest, setMergeRequest
 * Related Mapper    : MdmStewardWorkflowMapper
 * Related DB Table  : mdm_steward_workflows
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MdmStewardWorkflowController, MdmStewardWorkflowServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Bi Module. Implements MdmStewardWorkflowService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.bi.mdm;

import com.plus33.erp.bi.entity.MdmMergeRequest;
import com.plus33.erp.bi.entity.MdmStewardAssignment;
import com.plus33.erp.bi.entity.MdmStewardDecision;
import com.plus33.erp.bi.repository.MdmMergeRequestRepository;
import com.plus33.erp.bi.repository.MdmStewardAssignmentRepository;
import com.plus33.erp.bi.repository.MdmStewardDecisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code MdmStewardWorkflowService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.mdm}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Bi Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * MdmStewardWorkflowController
 *   --> MdmStewardWorkflowService (this)
 *   --> Validate business rules
 *   --> MdmStewardWorkflowRepository (read/write 'mdm_steward_workflows')
 *   --> MdmStewardWorkflowMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code mdm_steward_workflows}</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class MdmStewardWorkflowService {

    @Autowired MdmMergeRequestRepository mergeRequestRepo;
    @Autowired MdmStewardAssignmentRepository assignmentRepo;
    @Autowired MdmStewardDecisionRepository decisionRepo;
    /**
     * Creates a new assignment and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param mergeRequestId the mergeRequestId input value
     * @param stewardUser the stewardUser input value
     * @return the MdmStewardAssignment result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public MdmStewardAssignment createAssignment(Long mergeRequestId, String stewardUser) {
        MdmMergeRequest mr = mergeRequestRepo.findById(mergeRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Merge request not found: " + mergeRequestId));
        
        mr.setStatus("UNDER_REVIEW");
        mr.setUpdatedAt(LocalDateTime.now());
        mergeRequestRepo.save(mr);

        MdmStewardAssignment assignment = new MdmStewardAssignment();
        assignment.setMergeRequest(mr);
        assignment.setStewardUser(stewardUser);
        assignment.setAssignedAt(LocalDateTime.now());
        assignment.setDueAt(LocalDateTime.now().plusDays(3));
        assignment.setStatus("ASSIGNED");
        return assignmentRepo.save(assignment);
    }

    /**
     * Performs the resolveDecision operation in this module.
     *
     * @param assignmentId the assignmentId input value
     * @param decision the decision input value
     * @param notes the notes input value
     * @return the MdmStewardDecision result
     */
    @Transactional
    public MdmStewardDecision resolveDecision(Long assignmentId, String decision, String notes) {
        MdmStewardAssignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found: " + assignmentId));
        
        assignment.setStatus("COMPLETED");
        assignmentRepo.save(assignment);

        MdmMergeRequest mr = assignment.getMergeRequest();

        if ("APPROVE".equalsIgnoreCase(decision)) {
            mr.setStatus("APPROVED");
        } else if ("REJECT".equalsIgnoreCase(decision)) {
            mr.setStatus("REJECTED");
        } else {
            mr.setStatus("UNDER_REVIEW");
        }
        mr.setUpdatedAt(LocalDateTime.now());
        mergeRequestRepo.save(mr);

        MdmStewardDecision sd = new MdmStewardDecision();
        sd.setStewardAssignment(assignment);
        sd.setDecision(decision);
        sd.setNotes(notes);
        sd.setDecidedAt(LocalDateTime.now());
        return decisionRepo.save(sd);
    }
}