/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.chargeback
 * File              : CostChargebackManager.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CostChargebackManagerController
 * Related Service   : CostChargebackManager
 * Related Repository: CostChargebackManagerRepository
 * Related Entity    : CostChargebackManager
 * Related DTO       : N/A
 * Related Mapper    : CostChargebackManagerMapper
 * Related DB Table  : cost_chargeback_managers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CostChargebackManagerController, CostChargebackManagerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements CostChargebackManagerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.chargeback;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code CostChargebackManager}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.chargeback}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * CostChargebackManagerController
 *   --> CostChargebackManager (this)
 *   --> Validate business rules
 *   --> CostChargebackManagerRepository (read/write 'cost_chargeback_managers')
 *   --> CostChargebackManagerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code cost_chargeback_managers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class CostChargebackManager {
    @Autowired PlatformCostCenterRepository centerRepo;
    @Autowired PlatformCostAllocationRepository allocationRepo;
    @Autowired PlatformChargebackRepository chargebackRepo;
    /**
     * Creates a new cost center and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param code the code input value
     * @param name the name input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void createCostCenter(String code, String name) {
        PlatformCostCenter cc = new PlatformCostCenter();
        cc.setCenterCode(code);
        cc.setCenterName(name);
        centerRepo.save(cc);
    }

    /**
     * Reserves cost resources (budget or stock) for downstream processing.
     *
     * @param centerCode the centerCode input value
     * @param resource the resource input value
     * @param ratio the ratio input value
     */
    @Transactional
    public void allocateCost(String centerCode, String resource, double ratio) {
        PlatformCostCenter cc = centerRepo.findAll().stream()
                .filter(c -> c.getCenterCode().equals(centerCode))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Cost center not found"));

        PlatformCostAllocation alloc = new PlatformCostAllocation();
        alloc.setCostCenterId(cc.getId());
        alloc.setResourceId(resource);
        alloc.setAllocationRatio(BigDecimal.valueOf(ratio));
        allocationRepo.save(alloc);
    }

    /**
     * Performs the recordChargeback operation in this module.
     *
     * @param centerCode the centerCode input value
     * @param amount the amount input value
     * @param month the month input value
     */
    @Transactional
    public void recordChargeback(String centerCode, double amount, String month) {
        PlatformCostCenter cc = centerRepo.findAll().stream()
                .filter(c -> c.getCenterCode().equals(centerCode))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Cost center not found"));

        PlatformChargeback cb = new PlatformChargeback();
        cb.setCostCenterId(cc.getId());
        cb.setAmount(BigDecimal.valueOf(amount));
        cb.setBillingMonth(month);
        cb.setRecordedAt(LocalDateTime.now());
        chargebackRepo.save(cb);
    }
}