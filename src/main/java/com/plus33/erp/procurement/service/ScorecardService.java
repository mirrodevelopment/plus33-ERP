package com.plus33.erp.procurement.service;

import com.plus33.erp.procurement.entity.SupplierScorecard;
import com.plus33.erp.procurement.repository.SupplierScorecardRepository;
import com.plus33.erp.procurement.event.ProcurementEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ScorecardService {

    private final SupplierScorecardRepository scorecardRepository;
    private final ProcurementEventBus eventBus;

    public ScorecardService(SupplierScorecardRepository scorecardRepository, ProcurementEventBus eventBus) {
        this.scorecardRepository = scorecardRepository;
        this.eventBus = eventBus;
    }

    @Transactional
    public void recalculateScorecard(Long supplierId, BigDecimal otd, BigDecimal defects, BigDecimal invoiceAccuracy) {
        SupplierScorecard scorecard = scorecardRepository.findBySupplierId(supplierId)
                .orElseGet(() -> {
                    SupplierScorecard sc = new SupplierScorecard();
                    sc.setSupplierId(supplierId);
                    return sc;
                });

        scorecard.setOnTimeDeliveryRate(otd);
        scorecard.setQualityDefectRate(defects);
        scorecard.setInvoiceAccuracyRate(invoiceAccuracy);

        // Overall Score formula: OTD - defects + invoice accuracy / 2
        BigDecimal sum = otd.subtract(defects).add(invoiceAccuracy);
        scorecard.setOverallRating(sum.divide(new BigDecimal("2"), BigDecimal.ROUND_HALF_UP));
        scorecard.setRecalculatedAt(LocalDateTime.now());
        scorecardRepository.save(scorecard);

        eventBus.publish("SupplierEvaluated", 1L, supplierId, "Supplier performance scorecard updated: " + scorecard.getOverallRating());
    }
}
