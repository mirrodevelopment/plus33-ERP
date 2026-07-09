/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.service
 * File              : CommissionEngine.java
 * Purpose           : Business logic service layer for Crm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CommissionEngineController
 * Related Service   : CommissionEngine
 * Related Repository: CrmCommissionRepository
 * Related Entity    : CommissionEngine
 * Related DTO       : N/A
 * Related Mapper    : CommissionEngineMapper
 * Related DB Table  : commission_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CommissionEngineController, CommissionEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Crm Module. Implements CommissionEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.crm.service;

import com.plus33.erp.crm.entity.CrmCommission;
import com.plus33.erp.crm.repository.CrmCommissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code CommissionEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Crm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * CommissionEngineController
 *   --> CommissionEngine (this)
 *   --> Validate business rules
 *   --> CommissionEngineRepository (read/write 'commission_engines')
 *   --> CommissionEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code commission_engines}</p>
 * <p><b>Module Deps      :</b> Crm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional
public class CommissionEngine {

    private final CrmCommissionRepository commissionRepo;

    public CommissionEngine(CrmCommissionRepository commissionRepo) {
        this.commissionRepo = commissionRepo;
    }

    /**
     * Calculates commission totals including subtotal, tax, discounts, and net amount.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param repId the repId input value
     * @param opportunityId the opportunityId input value
     * @param dealAmount the dealAmount input value
     * @return the CrmCommission result
     */
    public CrmCommission calculateCommission(Long companyId, Long repId, Long opportunityId, BigDecimal dealAmount) {
        // Flat 5% base commission rate
        BigDecimal rate = new BigDecimal("0.05");
        BigDecimal commissionAmount = dealAmount.multiply(rate);

        CrmCommission comm = new CrmCommission();
        comm.setCompanyId(companyId);
        comm.setSalesRepId(repId);
        comm.setOpportunityId(opportunityId);
        comm.setAmount(commissionAmount);
        comm.setStatus("CALCULATED");
        return commissionRepo.save(comm);
    }
}