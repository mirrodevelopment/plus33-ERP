/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.controller
 * File              : BiMdmController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiMdmController
 * Related Service   : BiMdmControllerService, BiMdmControllerServiceImpl
 * Related Repository: BiMdmControllerRepository
 * Related Entity    : BiMdmController
 * Related DTO       : N/A
 * Related Mapper    : BiMdmControllerMapper
 * Related DB Table  : bi_mdm_controllers
 * Related REST APIs : POST /api/bi/mdm/evaluate, POST /api/bi/mdm/merge, POST /api/bi/mdm/assign-steward, POST /api/bi/mdm/resolve-decision
 * Depends On        : None
 * Used By           : Bi Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Bi Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/bi/mdm/evaluate, POST /api/bi/mdm/merge, POST /api/bi/mdm/assign-steward, POST /api/bi/mdm/resolve-decision
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiMdmController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to BiMdmService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> BiMdmController.endpoint()
 *   --> BiMdmService.method()
 *   --> BiMdmRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/bi/mdm/evaluate, POST /api/bi/mdm/merge, POST /api/bi/mdm/assign-steward, POST /api/bi/mdm/resolve-decision, GET /api/bi/mdm/golden-records</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/bi/mdm")
public class BiMdmController {

    @Autowired MdmGoldenRecordService goldenRecordService;
    @Autowired MdmStewardWorkflowService stewardWorkflowService;
    @Autowired MdmGoldenRecordRepository goldenRecordRepo;
    /**
     * Performs the evaluateRecord operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return the result string value
     */
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

    /**
     * Performs the executeMerge operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param mergeRequestId the mergeRequestId input value
     * @param stewardUser the stewardUser input value
     * @return the result string value
     */
    @PostMapping("/merge")
    public String executeMerge(@RequestParam Long mergeRequestId, @RequestParam String stewardUser) {
        goldenRecordService.executeMerge(mergeRequestId, stewardUser);
        return "Merge executed.";
    }

    /**
     * Performs the assignSteward operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param mergeRequestId the mergeRequestId input value
     * @param stewardUser the stewardUser input value
     * @return the MdmStewardAssignment result
     */
    @PostMapping("/assign-steward")
    public MdmStewardAssignment assignSteward(@RequestParam Long mergeRequestId, @RequestParam String stewardUser) {
        return stewardWorkflowService.createAssignment(mergeRequestId, stewardUser);
    }

    /**
     * Performs the resolveDecision operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param assignmentId the assignmentId input value
     * @param decision the decision input value
     * @param notes the notes input value
     * @return the MdmStewardDecision result
     */
    @PostMapping("/resolve-decision")
    public MdmStewardDecision resolveDecision(@RequestParam Long assignmentId, @RequestParam String decision, @RequestParam String notes) {
        return stewardWorkflowService.resolveDecision(assignmentId, decision, notes);
    }

    /**
     * Retrieves golden records data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/golden-records")
    public List<MdmGoldenRecord> getGoldenRecords() {
        return goldenRecordRepo.findAll();
    }
}