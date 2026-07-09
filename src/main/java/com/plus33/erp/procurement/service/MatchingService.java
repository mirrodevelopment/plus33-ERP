/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.service
 * File              : MatchingService.java
 * Purpose           : Business logic service layer for Procurement Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MatchingController
 * Related Service   : MatchingService
 * Related Repository: PurchaseOrderRepository
 * Related Entity    : Matching
 * Related DTO       : N/A
 * Related Mapper    : MatchingMapper
 * Related DB Table  : matchings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MatchingController, MatchingServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Procurement Module. Implements MatchingService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.procurement.service;

import com.plus33.erp.procurement.entity.PurchaseOrder;
import com.plus33.erp.procurement.repository.PurchaseOrderRepository;
import com.plus33.erp.procurement.event.ProcurementEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code MatchingService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Procurement Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * MatchingController
 *   --> MatchingService (this)
 *   --> Validate business rules
 *   --> MatchingRepository (read/write 'matchings')
 *   --> MatchingMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code matchings}</p>
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class MatchingService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProcurementEventBus eventBus;

    public MatchingService(PurchaseOrderRepository purchaseOrderRepository, ProcurementEventBus eventBus) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.eventBus = eventBus;
    }

    /**
     * Performs the performThreeWayMatch operation in this module.
     *
     * @param poId the poId input value
     * @param grnQty the grnQty input value
     * @param invoiceQty the invoiceQty input value
     * @return true if operation succeeded, false otherwise
     */
    @Transactional
    public boolean performThreeWayMatch(Long poId, BigDecimal grnQty, BigDecimal invoiceQty) {
        PurchaseOrder po = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new IllegalArgumentException("Purchase Order not found"));

        // Match PO vs GRN vs Invoice
        if (grnQty.compareTo(invoiceQty) != 0) {
            eventBus.publish("InspectionFailed", po.getCompany().getId(), poId, "Three-way match failed: Qty mismatch");
            return false;
        }

        eventBus.publish("InvoiceMatched", po.getCompany().getId(), poId, "Three-way match succeeded");
        return true;
    }

    /**
     * Performs the performFourWayMatch operation in this module.
     *
     * @param poId the poId input value
     * @param grnQty the grnQty input value
     * @param inspectionPassedQty the inspectionPassedQty input value
     * @param invoiceQty the invoiceQty input value
     * @return true if operation succeeded, false otherwise
     */
    @Transactional
    public boolean performFourWayMatch(Long poId, BigDecimal grnQty, BigDecimal inspectionPassedQty, BigDecimal invoiceQty) {
        PurchaseOrder po = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new IllegalArgumentException("Purchase Order not found"));

        if (grnQty.compareTo(invoiceQty) != 0 || inspectionPassedQty.compareTo(invoiceQty) != 0) {
            eventBus.publish("InspectionFailed", po.getCompany().getId(), poId, "Four-way match failed");
            return false;
        }

        eventBus.publish("InvoiceMatched", po.getCompany().getId(), poId, "Four-way match succeeded");
        return true;
    }
}