package com.plus33.erp.procurement.service;

import com.plus33.erp.procurement.entity.PurchaseOrder;
import com.plus33.erp.procurement.repository.PurchaseOrderRepository;
import com.plus33.erp.procurement.event.ProcurementEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class MatchingService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ProcurementEventBus eventBus;

    public MatchingService(PurchaseOrderRepository purchaseOrderRepository, ProcurementEventBus eventBus) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.eventBus = eventBus;
    }

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
