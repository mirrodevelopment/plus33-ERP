package com.plus33.erp.finance.budget.exception;

import com.plus33.erp.common.exception.BusinessException;

public class BudgetExceededException extends BusinessException {
    public BudgetExceededException(String message) {
        super(message);
    }
}
