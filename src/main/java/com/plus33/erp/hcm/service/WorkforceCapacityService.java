/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.service
 * File              : WorkforceCapacityService.java
 * Purpose           : Business logic service layer for Hcm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WorkforceCapacityController
 * Related Service   : WorkforceCapacityService
 * Related Repository: RosterRepository, ShiftPatternRepository
 * Related Entity    : WorkforceCapacity
 * Related DTO       : N/A
 * Related Mapper    : WorkforceCapacityMapper
 * Related DB Table  : workforce_capacitys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WorkforceCapacityController, WorkforceCapacityServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Hcm Module. Implements WorkforceCapacityService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.hcm.service;

import com.plus33.erp.hcm.entity.Roster;
import com.plus33.erp.hcm.entity.ShiftPattern;
import com.plus33.erp.hcm.repository.RosterRepository;
import com.plus33.erp.hcm.repository.ShiftPatternRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code WorkforceCapacityService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Hcm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * WorkforceCapacityController
 *   --> WorkforceCapacityService (this)
 *   --> Validate business rules
 *   --> WorkforceCapacityRepository (read/write 'workforce_capacitys')
 *   --> WorkforceCapacityMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code workforce_capacitys}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class WorkforceCapacityService {

    private final RosterRepository rosterRepository;
    private final ShiftPatternRepository shiftPatternRepository;

    public WorkforceCapacityService(RosterRepository rosterRepository, ShiftPatternRepository shiftPatternRepository) {
        this.rosterRepository = rosterRepository;
        this.shiftPatternRepository = shiftPatternRepository;
    }

    /**
     * Calculates available hours totals including subtotal, tax, discounts, and net amount.
     *
     * @param employeeId the employeeId input value
     * @return the BigDecimal result
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateAvailableHours(Long employeeId) {
        List<Roster> rosters = rosterRepository.findByEmployeeId(employeeId);
        BigDecimal total = BigDecimal.ZERO;
        for (Roster r : rosters) {
            ShiftPattern pattern = shiftPatternRepository.findById(r.getShiftPatternId()).orElse(null);
            if (pattern != null) {
                // Return daily share of weekly shift hours (approx. 8 hours)
                total = total.add(pattern.getWeeklyHours().divide(new BigDecimal("5"), 2, RoundingMode.HALF_UP));
            }
        }
        return total;
    }
}