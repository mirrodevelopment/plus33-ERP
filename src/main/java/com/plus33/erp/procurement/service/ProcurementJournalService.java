package com.plus33.erp.procurement.service;

import com.plus33.erp.finance.service.InventoryJournalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class ProcurementJournalService {

    private static final Logger log = LoggerFactory.getLogger(ProcurementJournalService.class);
    private final InventoryJournalService inventoryJournalService;

    public ProcurementJournalService(InventoryJournalService inventoryJournalService) {
        this.inventoryJournalService = inventoryJournalService;
    }

    public void postPurchaseCommitment(Long companyId, BigDecimal amount, String poNumber) {
        log.info("SRM Posting: Purchase Commitment - companyId={}, amount={}, PO={}", companyId, amount, poNumber);
        inventoryJournalService.postInventoryMovementJournal(companyId, "PURCHASE_COMMITMENT", amount, poNumber);
    }

    public void postAccrual(Long companyId, BigDecimal amount, String grnNumber) {
        log.info("SRM Posting: Accrual (GRNI) - companyId={}, amount={}, GRN={}", companyId, amount, grnNumber);
        inventoryJournalService.postInventoryMovementJournal(companyId, "PURCHASE_ACCRUAL", amount, grnNumber);
    }
}
