/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.service
 * File              : ServiceBillingService.java
 * Purpose           : Business logic service layer for Esm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ServiceBillingController
 * Related Service   : ServiceBillingService
 * Related Repository: ServiceBillingRecordRepository
 * Related Entity    : ServiceBilling
 * Related DTO       : N/A
 * Related Mapper    : ServiceBillingMapper
 * Related DB Table  : service_billings
 * Related REST APIs : N/A
 * Depends On        : Finance Module
 * Used By           : ServiceBillingController, ServiceBillingServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Esm Module. Implements ServiceBillingService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.esm.service;

import com.plus33.erp.esm.entity.ServiceBillingRecord;
import com.plus33.erp.esm.repository.ServiceBillingRecordRepository;
import com.plus33.erp.esm.event.EsmEventBus;
import com.plus33.erp.finance.service.InventoryJournalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code ServiceBillingService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Esm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ServiceBillingController
 *   --> ServiceBillingService (this)
 *   --> Validate business rules
 *   --> ServiceBillingRepository (read/write 'service_billings')
 *   --> ServiceBillingMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code service_billings}</p>
 * <p><b>Module Deps      :</b> Esm, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Calculates and create billing totals including subtotal, tax, discounts, and net amount.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param workOrderId the workOrderId input value
     * @param method the method input value
     * @param baseAmount the baseAmount input value
     * @return the ServiceBillingRecord result
     */
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

    /**
     * Posts billing entries to the General Ledger and updates financial balances.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param billingRecordId the billingRecordId input value
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Performs the reverseBilling operation in this module.
     *
     * @param billingRecordId the billingRecordId input value
     */
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