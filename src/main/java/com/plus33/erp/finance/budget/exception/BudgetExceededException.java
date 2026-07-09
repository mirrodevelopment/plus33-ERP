/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.exception
 * File              : BudgetExceededException.java
 * Purpose           : Custom exception for domain error handling in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetExceededExceptionController
 * Related Service   : BudgetExceededExceptionService, BudgetExceededExceptionServiceImpl
 * Related Repository: BudgetExceededExceptionRepository
 * Related Entity    : BudgetExceededException
 * Related DTO       : N/A
 * Related Mapper    : BudgetExceededExceptionMapper
 * Related DB Table  : budget_exceeded_exceptions
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.budget.exception;

import com.plus33.erp.common.exception.BusinessException;

public class BudgetExceededException extends BusinessException {
    public BudgetExceededException(String message) {
        super(message);
    }
}
