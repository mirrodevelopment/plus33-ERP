/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.service
 * File              : TaxEngineRegistryImpl.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxEngineRegistryController
 * Related Service   : TaxEngineRegistryService, TaxEngineRegistryServiceImpl
 * Related Repository: TaxEngineRegistryRepository
 * Related Entity    : TaxEngineRegistry
 * Related DTO       : N/A
 * Related Mapper    : TaxEngineRegistryMapper
 * Related DB Table  : tax_engine_registrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.tax.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxEngineRegistryImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.service}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class TaxEngineRegistryImpl implements TaxEngineRegistry {

    private final Map<String, TaxEngineProvider> registry = new ConcurrentHashMap<>();

    public TaxEngineRegistryImpl(List<TaxEngineProvider> providers) {
        for (TaxEngineProvider provider : providers) {
            register(provider.getTaxType(), provider);
        }
    }

    /**
     * Performs the resolve operation in this module.
     *
     * @param taxCategoryCode the taxCategoryCode input value
     * @return the TaxEngineProvider result
     */
    /**
     * Performs the resolve operation in this module.
     *
     * @param taxCategoryCode the taxCategoryCode input value
     * @return the TaxEngineProvider result
     */
    @Override
    public TaxEngineProvider resolve(String taxCategoryCode) {
        if (taxCategoryCode == null) {
            return registry.get("VAT");
        }
        TaxEngineProvider provider = registry.get(taxCategoryCode.toUpperCase());
        if (provider == null) {
            // Fallback to VAT as default engine
            return registry.get("VAT");
        }
        return provider;
    }

    /**
     * Creates a new finance and persists it to the database.
     *
     * @param taxCategoryCode the taxCategoryCode input value
     * @param provider the provider input value
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Creates a new finance and persists it to the database.
     *
     * @param taxCategoryCode the taxCategoryCode input value
     * @param provider the provider input value
     * @throws BusinessException if a business rule is violated
     */
    @Override
    public void register(String taxCategoryCode, TaxEngineProvider provider) {
        registry.put(taxCategoryCode.toUpperCase(), provider);
    }
}