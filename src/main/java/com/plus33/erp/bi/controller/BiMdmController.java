package com.plus33.erp.bi.controller;

import com.plus33.erp.bi.entity.MdmGoldenRecord;
import com.plus33.erp.bi.entity.MdmStewardAssignment;
import com.plus33.erp.bi.entity.MdmStewardDecision;
import com.plus33.erp.bi.mdm.MdmGoldenRecordService;
import com.plus33.erp.bi.mdm.MdmStewardWorkflowService;
import com.plus33.erp.bi.repository.MdmGoldenRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bi/mdm")
public class BiMdmController {

    @Autowired MdmGoldenRecordService goldenRecordService;
    @Autowired MdmStewardWorkflowService stewardWorkflowService;
    @Autowired MdmGoldenRecordRepository goldenRecordRepo;

    @PostMapping("/evaluate")
    public String evaluateRecord(
            @RequestParam String recordType,
            @RequestParam String system,
            @RequestParam String table,
            @RequestParam Long dimId,
            @RequestParam String displayName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String taxNumber) {
        goldenRecordService.evaluateIncomingRecord(recordType, system, table, dimId, displayName, email, phone, address, taxNumber);
        return "Record evaluated.";
    }

    @PostMapping("/merge")
    public String executeMerge(@RequestParam Long mergeRequestId, @RequestParam String stewardUser) {
        goldenRecordService.executeMerge(mergeRequestId, stewardUser);
        return "Merge executed.";
    }

    @PostMapping("/assign-steward")
    public MdmStewardAssignment assignSteward(@RequestParam Long mergeRequestId, @RequestParam String stewardUser) {
        return stewardWorkflowService.createAssignment(mergeRequestId, stewardUser);
    }

    @PostMapping("/resolve-decision")
    public MdmStewardDecision resolveDecision(@RequestParam Long assignmentId, @RequestParam String decision, @RequestParam String notes) {
        return stewardWorkflowService.resolveDecision(assignmentId, decision, notes);
    }

    @GetMapping("/golden-records")
    public List<MdmGoldenRecord> getGoldenRecords() {
        return goldenRecordRepo.findAll();
    }
}