package com.plus33.erp.esm.service;

import com.plus33.erp.esm.entity.ServiceBillingRecord;
import com.plus33.erp.esm.repository.ServiceBillingRecordRepository;
import com.plus33.erp.esm.event.EsmEventBus;
import com.plus33.erp.finance.service.InventoryJournalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ServiceBillingService {

    private final ServiceBillingRecordRepository billingRecordRepository;
    private final InventoryJournalService inventoryJournalService;
    private final EsmEventBus eventBus;

    public ServiceBillingService(ServiceBillingRecordRepository billingRecordRepository,
                                 InventoryJournalService inventoryJournalService,
                                 EsmEventBus eventBus) {
        this.billingRecordRepository = billingRecordRepository;
        this.inventoryJournalService = inventoryJournalService;
        this.eventBus = eventBus;
    }

    @Transactional
    public ServiceBillingRecord calculateAndCreateBilling(Long companyId, Long workOrderId, String method, BigDecimal baseAmount) {
        ServiceBillingRecord record = new ServiceBillingRecord();
        record.setCompanyId(companyId);
        record.setWorkOrderId(workOrderId);
        record.setBillingMethod(method);
        record.setAmount(baseAmount);
        record.setStatus("READY_TO_BILL");

        if ("WARRANTY".equals(method)) {
            record.setStatus("NOT_BILLABLE");
            record.setAmount(BigDecimal.ZERO);
        }

        billingRecordRepository.save(record);
        return record;
    }

    @Transactional
    public void postBilling(Long billingRecordId) {
        ServiceBillingRecord record = billingRecordRepository.findById(billingRecordId)
                .orElseThrow(() -> new IllegalArgumentException("Billing Record not found"));

        if (!"READY_TO_BILL".equals(record.getStatus())) {
            throw new IllegalStateException("Only READY_TO_BILL records can be billed and posted");
        }

        record.setStatus("BILLED");
        billingRecordRepository.save(record);

        record.setStatus("POSTED");
        billingRecordRepository.save(record);

        // Call GL Posting
        inventoryJournalService.postInventoryMovementJournal(
                record.getCompanyId(),
                "SERVICE_BILLING",
                record.getAmount(),
                "WO-BILL-" + record.getWorkOrderId()
        );

        eventBus.publish("InvoiceGenerated", record.getCompanyId(), record.getWorkOrderId(), "Service invoice posted: " + record.getAmount());
    }

    @Transactional
    public void reverseBilling(Long billingRecordId) {
        ServiceBillingRecord record = billingRecordRepository.findById(billingRecordId)
                .orElseThrow(() -> new IllegalArgumentException("Billing Record not found"));

        if (!"POSTED".equals(record.getStatus())) {
            throw new IllegalStateException("Only POSTED records can be reversed");
        }

        record.setStatus("REVERSED");
        billingRecordRepository.save(record);

        // Reverse GL Posting
        inventoryJournalService.postInventoryMovementJournal(
                record.getCompanyId(),
                "SERVICE_BILLING_REVERSAL",
                record.getAmount().negate(),
                "WO-BILL-REV-" + record.getWorkOrderId()
        );
    }
}
