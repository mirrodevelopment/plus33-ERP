package com.plus33.erp.crm.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PricingCalculationService {

    public BigDecimal calculateLinePrice(BigDecimal qty, BigDecimal basePrice, BigDecimal discountPct, BigDecimal taxPct) {
        BigDecimal sub = qty.multiply(basePrice).setScale(2, RoundingMode.HALF_UP);
        BigDecimal disc = sub.multiply(discountPct).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        BigDecimal net = sub.subtract(disc);
        BigDecimal tax = net.multiply(taxPct).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return net.add(tax).setScale(2, RoundingMode.HALF_UP);
    }
}
