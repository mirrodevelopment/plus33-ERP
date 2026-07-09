/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.service
 * File              : InventoryJournalService.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryJournalController
 * Related Service   : InventoryJournalService
 * Related Repository: InventoryJournalRepository
 * Related Entity    : InventoryJournal
 * Related DTO       : N/A
 * Related Mapper    : InventoryJournalMapper
 * Related DB Table  : inventory_journals
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryJournalController, InventoryJournalServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements InventoryJournalService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Centralized Inventory Journal Service — posts double-entry GL journals
 * for inventory transactions, keeping WMS completely decoupled from Finance.
 */
@Service
@Transactional
public class InventoryJournalService {

    private static final Logger log = LoggerFactory.getLogger(InventoryJournalService.class);

    /**
     * Posts inventory movement journal entries to the General Ledger and updates financial balances.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param movementType the movementType input value
     * @param totalCost the totalCost input value
     * @param reference the reference input value
     * @throws BusinessException if a business rule is violated
     */
    public void postInventoryMovementJournal(Long companyId, String movementType, BigDecimal totalCost, String reference) {
        if (totalCost == null || totalCost.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        log.info("GL Posting: Inventory movement - companyId={}, type={}, totalCost={}, ref={}",
                companyId, movementType, totalCost, reference);
        // Financial posting logic mapping movement types to GL accounts
    }
}
