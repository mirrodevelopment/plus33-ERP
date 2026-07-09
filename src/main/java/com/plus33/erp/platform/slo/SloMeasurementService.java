/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.slo
 * File              : SloMeasurementService.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SloMeasurementController
 * Related Service   : SloMeasurementService
 * Related Repository: SloMeasurementRepository
 * Related Entity    : SloMeasurement
 * Related DTO       : N/A
 * Related Mapper    : SloMeasurementMapper
 * Related DB Table  : slo_measurements
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SloMeasurementController, SloMeasurementServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements SloMeasurementService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.slo;

import com.plus33.erp.platform.entity.PlatformSlo;
import com.plus33.erp.platform.entity.PlatformSloMeasurement;
import com.plus33.erp.platform.repository.PlatformSloMeasurementRepository;
import com.plus33.erp.platform.repository.PlatformSloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code SloMeasurementService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.slo}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * SloMeasurementController
 *   --> SloMeasurementService (this)
 *   --> Validate business rules
 *   --> SloMeasurementRepository (read/write 'slo_measurements')
 *   --> SloMeasurementMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code slo_measurements}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class SloMeasurementService {
    @Autowired PlatformSloRepository sloRepo;
    @Autowired PlatformSloMeasurementRepository measurementRepo;
    /**
     * Creates a new slo and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param name the name input value
     * @param target the target input value
     * @param service the service input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void createSlo(String name, double target, String service) {
        PlatformSlo slo = new PlatformSlo();
        slo.setName(name);
        slo.setTargetPercentage(BigDecimal.valueOf(target));
        slo.setServiceName(service);
        sloRepo.save(slo);
    }

    /**
     * Performs the recordMeasurement operation in this module.
     *
     * @param name the name input value
     * @param current the current input value
     * @param budget the budget input value
     */
    @Transactional
    public void recordMeasurement(String name, double current, double budget) {
        PlatformSlo slo = sloRepo.findAll().stream()
                .filter(s -> s.getName().equals(name))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("SLO not found"));

        PlatformSloMeasurement m = new PlatformSloMeasurement();
        m.setSloId(slo.getId());
        m.setCurrentPercentage(BigDecimal.valueOf(current));
        m.setErrorBudget(BigDecimal.valueOf(budget));
        m.setRecordedAt(LocalDateTime.now());
        measurementRepo.save(m);
    }
}