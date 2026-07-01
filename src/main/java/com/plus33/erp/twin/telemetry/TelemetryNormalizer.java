package com.plus33.erp.twin.telemetry;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class TelemetryNormalizer {
    public BigDecimal normalize(String name, BigDecimal val) {
        // Normalizes values for uniform processing
        return val;
    }
}