package com.plus33.erp.twin.telemetry;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class TelemetryValidator {
    public boolean isValid(String name, BigDecimal val) {
        return name != null && val != null && val.compareTo(BigDecimal.ZERO) >= 0;
    }
}