/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.service
 * File              : ScorecardService.java
 * Purpose           : Business logic service layer for Procurement Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ScorecardController
 * Related Service   : ScorecardService
 * Related Repository: SupplierScorecardRepository
 * Related Entity    : Scorecard
 * Related DTO       : N/A
 * Related Mapper    : ScorecardMapper
 * Related DB Table  : scorecards
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ScorecardController, ScorecardServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Procurement Module. Implements ScorecardService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.procurement.service;

import com.plus33.erp.procurement.entity.SupplierScorecard;
import com.plus33.erp.procurement.repository.SupplierScorecardRepository;
import com.plus33.erp.procurement.event.ProcurementEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code ScorecardService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Procurement Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ScorecardController
 *   --> ScorecardService (this)
 *   --> Validate business rules
 *   --> ScorecardRepository (read/write 'scorecards')
 *   --> ScorecardMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code scorecards}</p>
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ScorecardService {

    private final SupplierScorecardRepository scorecardRepository;
    private final ProcurementEventBus eventBus;

    public ScorecardService(SupplierScorecardRepository scorecardRepository, ProcurementEventBus eventBus) {
        this.scorecardRepository = scorecardRepository;
        this.eventBus = eventBus;
    }

    /**
     * Performs the recalculateScorecard operation in this module.
     *
     * @param supplierId the supplierId input value
     * @param otd the otd input value
     * @param defects the defects input value
     * @param invoiceAccuracy the invoiceAccuracy input value
     */
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