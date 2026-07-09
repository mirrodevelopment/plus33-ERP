/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.payroll
 * File              : PayrollEngineRegistryImpl.java
 * Purpose           : Component of Workforce Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollEngineRegistryController
 * Related Service   : PayrollEngineRegistryService, PayrollEngineRegistryServiceImpl
 * Related Repository: PayrollEngineRegistryRepository
 * Related Entity    : PayrollEngineRegistry
 * Related DTO       : N/A
 * Related Mapper    : PayrollEngineRegistryMapper
 * Related DB Table  : payroll_engine_registrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Workforce Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Workforce Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.workforce.payroll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollEngineRegistryImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.payroll}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Workforce Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class PayrollEngineRegistryImpl implements PayrollEngineRegistry {

    private final Map<String, PayrollEngineProvider> providers = new ConcurrentHashMap<>();

    @Autowired
    public PayrollEngineRegistryImpl(List<PayrollEngineProvider> providerList) {
        if (providerList != null) {
            for (PayrollEngineProvider p : providerList) {
                registerProvider(p);
            }
        }
    }

    /**
     * Creates a new provider and persists it to the database.
     *
     * @param provider the provider input value
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Creates a new provider and persists it to the database.
     *
     * @param provider the provider input value
     * @throws BusinessException if a business rule is violated
     */
    @Override
    public void registerProvider(PayrollEngineProvider provider) {
        if (provider != null && provider.getCountryCode() != null) {
            providers.put(provider.getCountryCode().toUpperCase(), provider);
        }
    }

    /**
     * Retrieves provider data from the database.
     *
     * @param countryCode the countryCode input value
     * @return the PayrollEngineProvider result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves provider data from the database.
     *
     * @param countryCode the countryCode input value
     * @return the PayrollEngineProvider result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public PayrollEngineProvider getProvider(String countryCode) {
        if (countryCode == null) {
            return providers.get("US");
        }
        PayrollEngineProvider provider = providers.get(countryCode.toUpperCase());
        return provider != null ? provider : providers.get("US");
    }
}