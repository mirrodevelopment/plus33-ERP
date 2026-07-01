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

@Service
public class MdmStewardWorkflowService {

    @Autowired MdmMergeRequestRepository mergeRequestRepo;
    @Autowired MdmStewardAssignmentRepository assignmentRepo;
    @Autowired MdmStewardDecisionRepository decisionRepo;

    @Transactional
    public MdmStewardAssignment createAssignment(Long mergeRequestId, String stewardUser) {
        MdmMergeRequest mr = mergeRequestRepo.findById(mergeRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Merge request not found: " + mergeRequestId));
        
        mr.setStatus("UNDER_REVIEW");
        mr.setUpdatedAt(LocalDateTime.now());
        mergeRequestRepo.save(mr);

        MdmStewardAssignment assignment = new MdmStewardAssignment();
        assignment.setMergeRequestId(mergeRequestId);
        assignment.setStewardUser(stewardUser);
        assignment.setAssignedAt(LocalDateTime.now());
        assignment.setDueAt(LocalDateTime.now().plusDays(3));
        assignment.setStatus("ASSIGNED");
        return assignmentRepo.save(assignment);
    }

    @Transactional
    public MdmStewardDecision resolveDecision(Long assignmentId, String decision, String notes) {
        MdmStewardAssignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found: " + assignmentId));
        
        assignment.setStatus("COMPLETED");
        assignmentRepo.save(assignment);

        MdmMergeRequest mr = mergeRequestRepo.findById(assignment.getMergeRequestId())
                .orElseThrow(() -> new IllegalArgumentException("Merge request not found"));

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
        sd.setStewardAssignmentId(assignmentId);
        sd.setDecision(decision);
        sd.setNotes(notes);
        sd.setDecidedAt(LocalDateTime.now());
        return decisionRepo.save(sd);
    }
}