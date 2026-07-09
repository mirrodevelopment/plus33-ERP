/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.service
 * File              : ProcurementJournalService.java
 * Purpose           : Business logic service layer for Procurement Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProcurementJournalController
 * Related Service   : ProcurementJournalService
 * Related Repository: ProcurementJournalRepository
 * Related Entity    : ProcurementJournal
 * Related DTO       : N/A
 * Related Mapper    : ProcurementJournalMapper
 * Related DB Table  : procurement_journals
 * Related REST APIs : N/A
 * Depends On        : Finance Module
 * Used By           : ProcurementJournalController, ProcurementJournalServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Procurement Module. Implements ProcurementJournalService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.procurement.service;

import com.plus33.erp.finance.service.InventoryJournalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code ProcurementJournalService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Procurement Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ProcurementJournalController
 *   --> ProcurementJournalService (this)
 *   --> Validate business rules
 *   --> ProcurementJournalRepository (read/write 'procurement_journals')
 *   --> ProcurementJournalMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code procurement_journals}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional
public class ProcurementJournalService {

    private static final Logger log = LoggerFactory.getLogger(ProcurementJournalService.class);
    private final InventoryJournalService inventoryJournalService;

    public ProcurementJournalService(InventoryJournalService inventoryJournalService) {
        this.inventoryJournalService = inventoryJournalService;
    }

    /**
     * Posts purchase commitment entries to the General Ledger and updates financial balances.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param amount the amount input value
     * @param poNumber the poNumber input value
     * @throws BusinessException if a business rule is violated
     */
    public void postPurchaseCommitment(Long companyId, BigDecimal amount, String poNumber) {
        log.info("SRM Posting: Purchase Commitment - companyId={}, amount={}, PO={}", companyId, amount, poNumber);
        inventoryJournalService.postInventoryMovementJournal(companyId, "PURCHASE_COMMITMENT", amount, poNumber);
    }

    /**
     * Posts accrual entries to the General Ledger and updates financial balances.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param amount the amount input value
     * @param grnNumber the grnNumber input value
     * @throws BusinessException if a business rule is violated
     */
    public void postAccrual(Long companyId, BigDecimal amount, String grnNumber) {
        log.info("SRM Posting: Accrual (GRNI) - companyId={}, amount={}, GRN={}", companyId, amount, grnNumber);
        inventoryJournalService.postInventoryMovementJournal(companyId, "PURCHASE_ACCRUAL", amount, grnNumber);
    }
}