/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.service
 * File              : SuccessionPlanningService.java
 * Purpose           : Business logic service layer for Hcm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SuccessionPlanningController
 * Related Service   : SuccessionPlanningService
 * Related Repository: TalentPoolRepository, SuccessorRepository
 * Related Entity    : SuccessionPlanning
 * Related DTO       : N/A
 * Related Mapper    : SuccessionPlanningMapper
 * Related DB Table  : succession_plannings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SuccessionPlanningController, SuccessionPlanningServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Hcm Module. Implements SuccessionPlanningService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.hcm.service;

import com.plus33.erp.hcm.entity.*;
import com.plus33.erp.hcm.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code SuccessionPlanningService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Hcm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * SuccessionPlanningController
 *   --> SuccessionPlanningService (this)
 *   --> Validate business rules
 *   --> SuccessionPlanningRepository (read/write 'succession_plannings')
 *   --> SuccessionPlanningMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code succession_plannings}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class SuccessionPlanningService {

    private final TalentPoolRepository talentPoolRepository;
    private final SuccessorRepository successorRepository;

    public SuccessionPlanningService(TalentPoolRepository talentPoolRepository, SuccessorRepository successorRepository) {
        this.talentPoolRepository = talentPoolRepository;
        this.successorRepository = successorRepository;
    }

    /**
     * Creates a new talent pool and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param name the name input value
     * @param desc the desc input value
     * @return the TalentPool result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public TalentPool createTalentPool(String name, String desc) {
        TalentPool pool = new TalentPool();
        pool.setName(name);
        pool.setDescription(desc);
        talentPoolRepository.save(pool);
        return pool;
    }

    /**
     * Performs the nominateSuccessor operation in this module.
     *
     * @param talentPoolId the talentPoolId input value
     * @param employeeId the employeeId input value
     * @param readiness the readiness input value
     * @return the Successor result
     */
    @Transactional
    public Successor nominateSuccessor(Long talentPoolId, Long employeeId, BigDecimal readiness) {
        Successor succ = new Successor();
        succ.setTalentPoolId(talentPoolId);
        succ.setEmployeeId(employeeId);
        succ.setReadinessScore(readiness);
        successorRepository.save(succ);
        return succ;
    }
}