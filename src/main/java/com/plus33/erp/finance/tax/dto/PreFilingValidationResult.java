package com.plus33.erp.finance.tax.dto;

import lombok.Value;
import java.util.List;

@Value
public class PreFilingValidationResult {
    List<String> errors;
    List<String> warnings;

    public boolean isValid() {
        return errors.isEmpty();
    }
}
