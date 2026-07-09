/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.service
 * File              : PricingCalculationService.java
 * Purpose           : Business logic service layer for Crm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PricingCalculationController
 * Related Service   : PricingCalculationService
 * Related Repository: PricingCalculationRepository
 * Related Entity    : PricingCalculation
 * Related DTO       : N/A
 * Related Mapper    : PricingCalculationMapper
 * Related DB Table  : pricing_calculations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PricingCalculationController, PricingCalculationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Crm Module. Implements PricingCalculationService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.crm.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code PricingCalculationService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Crm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PricingCalculationController
 *   --> PricingCalculationService (this)
 *   --> Validate business rules
 *   --> PricingCalculationRepository (read/write 'pricing_calculations')
 *   --> PricingCalculationMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code pricing_calculations}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class PricingCalculationService {

    /**
     * Calculates line price totals including subtotal, tax, discounts, and net amount.
     *
     * @param qty the qty input value
     * @param basePrice the basePrice input value
     * @param discountPct the discountPct input value
     * @param taxPct the taxPct input value
     * @return the BigDecimal result
     */
    public BigDecimal calculateLinePrice(BigDecimal qty, BigDecimal basePrice, BigDecimal discountPct, BigDecimal taxPct) {
        BigDecimal sub = qty.multiply(basePrice).setScale(2, RoundingMode.HALF_UP);
        BigDecimal disc = sub.multiply(discountPct).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal net = sub.subtract(disc);
        BigDecimal tax = net.multiply(taxPct).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return net.add(tax).setScale(2, RoundingMode.HALF_UP);
    }
}