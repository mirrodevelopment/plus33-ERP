/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.service
 * File              : TaxConfigurationCacheEvictAspect.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxConfigurationCacheEvictAspectController
 * Related Service   : TaxConfigurationCacheEvictAspectService, TaxConfigurationCacheEvictAspectServiceImpl
 * Related Repository: TaxConfigurationCacheEvictAspectRepository
 * Related Entity    : TaxConfigurationCacheEvictAspect
 * Related DTO       : N/A
 * Related Mapper    : TaxConfigurationCacheEvictAspectMapper
 * Related DB Table  : tax_configuration_cache_evict_aspects
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.tax.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxConfigurationCacheEvictAspect}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.service}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class TaxConfigurationCacheEvictAspect {

    private final TaxConfigurationCache taxConfigurationCache;

    /**
     * Performs the evictTaxCache operation in this module.
     *
     */
    @AfterReturning("execution(* com.plus33.erp.finance.tax.repository.TaxConfigurationVersionRepository.save*(..)) || execution(* com.plus33.erp.finance.tax.repository.TaxConfigurationVersionRepository.delete*(..))")
    public void evictTaxCache() {
        log.info("Evicting Tax Rates and Rules Cache due to TaxConfigurationVersion modification");
        taxConfigurationCache.invalidate();
    }
}