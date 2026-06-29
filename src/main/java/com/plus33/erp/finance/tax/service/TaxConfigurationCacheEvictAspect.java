package com.plus33.erp.finance.tax.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class TaxConfigurationCacheEvictAspect {

    private final TaxConfigurationCache taxConfigurationCache;

    @AfterReturning("execution(* com.plus33.erp.finance.tax.repository.TaxConfigurationVersionRepository.save*(..)) || execution(* com.plus33.erp.finance.tax.repository.TaxConfigurationVersionRepository.delete*(..))")
    public void evictTaxCache() {
        log.info("Evicting Tax Rates and Rules Cache due to TaxConfigurationVersion modification");
        taxConfigurationCache.invalidate();
    }
}
