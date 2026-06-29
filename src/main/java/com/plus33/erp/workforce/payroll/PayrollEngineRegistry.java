package com.plus33.erp.workforce.payroll;

import java.util.Optional;

public interface PayrollEngineRegistry {
    void registerProvider(PayrollEngineProvider provider);
    PayrollEngineProvider getProvider(String countryCode);
}
