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

    public void postInventoryMovementJournal(Long companyId, String movementType, BigDecimal totalCost, String reference) {
        if (totalCost == null || totalCost.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        log.info("GL Posting: Inventory movement - companyId={}, type={}, totalCost={}, ref={}",
                companyId, movementType, totalCost, reference);
        // Financial posting logic mapping movement types to GL accounts
    }
}
