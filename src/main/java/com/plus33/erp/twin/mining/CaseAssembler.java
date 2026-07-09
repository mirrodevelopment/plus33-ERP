/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Twin Module
 * Package           : com.plus33.erp.twin.mining
 * File              : CaseAssembler.java
 * Purpose           : Business logic service layer for Twin Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CaseAssemblerController
 * Related Service   : CaseAssembler
 * Related Repository: CaseAssemblerRepository
 * Related Entity    : CaseAssembler
 * Related DTO       : N/A
 * Related Mapper    : CaseAssemblerMapper
 * Related DB Table  : case_assemblers
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : CaseAssemblerController, CaseAssemblerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Twin Module. Implements CaseAssemblerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.twin.mining;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Twin Module</b>
 *
 * <p><b>Class  :</b> {@code CaseAssembler}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.twin.mining}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Twin Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * CaseAssemblerController
 *   --> CaseAssembler (this)
 *   --> Validate business rules
 *   --> CaseAssemblerRepository (read/write 'case_assemblers')
 *   --> CaseAssemblerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code case_assemblers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class CaseAssembler {
    @Autowired PlatformProcessCaseRepository caseRepo;
    /**
     * Performs the assembleCase operation in this module.
     *
     * @param token JWT Bearer token string
     * @param processName the processName input value
     * @return the PlatformProcessCase result
     */
    @Transactional
    public PlatformProcessCase assembleCase(String token, String processName) {
        return caseRepo.findAll().stream()
                .filter(c -> c.getCaseToken().equals(token))
                .findFirst()
                .orElseGet(() -> {
                    PlatformProcessCase c = new PlatformProcessCase();
                    c.setCaseToken(token);
                    c.setProcessName(processName);
                    c.setActive(true);
                    return caseRepo.save(c);
                });
    }
}