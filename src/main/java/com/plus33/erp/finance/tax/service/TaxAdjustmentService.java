/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.service
 * File              : TaxAdjustmentService.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxAdjustmentController
 * Related Service   : TaxAdjustmentService, TaxAdjustmentServiceImpl
 * Related Repository: TaxAdjustmentRepository
 * Related Entity    : TaxAdjustment
 * Related DTO       : N/A
 * Related Mapper    : TaxAdjustmentMapper
 * Related DB Table  : tax_adjustments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.tax.service;

import com.plus33.erp.finance.tax.entity.TaxAdjustmentEntry;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxAdjustmentService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.service}</p>
 * <p><b>Layer  :</b> Component of Finance Module in the PLUS33 Coffee ERP platform.</p>
 *
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface TaxAdjustmentService {
    TaxAdjustmentEntry createAdjustment(
            Long companyId,
            LocalDate date,
            Long taxCategoryId,
            Long glAccountId,
            BigDecimal debitAmount,
            BigDecimal creditAmount,
            String description,
            String reasonCode
    );

    TaxAdjustmentEntry approveAdjustment(Long adjustmentId, String approvedBy);

    List<TaxAdjustmentEntry> getAdjustmentsByCompany(Long companyId);
}
