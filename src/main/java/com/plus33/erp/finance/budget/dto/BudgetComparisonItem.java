/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.dto
 * File              : BudgetComparisonItem.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetComparisonItemController
 * Related Service   : BudgetComparisonItemService, BudgetComparisonItemServiceImpl
 * Related Repository: BudgetComparisonItemRepository
 * Related Entity    : BudgetComparisonItem
 * Related DTO       : BudgetDimensionSetResponse
 * Related Mapper    : BudgetComparisonItemMapper
 * Related DB Table  : budget_comparison_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;

public record BudgetComparisonItem(
    Long accountId,
    String accountCode,
    String accountName,
    BudgetDimensionSetResponse dimensionSet,
    BigDecimal allocatedAmount1,
    BigDecimal reservedAmount1,
    BigDecimal consumedAmount1,
    BigDecimal availableAmount1,
    BigDecimal allocatedAmount2,
    BigDecimal reservedAmount2,
    BigDecimal consumedAmount2,
    BigDecimal availableAmount2,
    BigDecimal varianceAmount,
    BigDecimal variancePercent
) {}
