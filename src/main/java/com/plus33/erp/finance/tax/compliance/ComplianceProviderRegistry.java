/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.compliance
 * File              : ComplianceProviderRegistry.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ComplianceProviderRegistryController
 * Related Service   : ComplianceProviderRegistryService, ComplianceProviderRegistryServiceImpl
 * Related Repository: ComplianceProviderRegistryRepository
 * Related Entity    : ComplianceProviderRegistry
 * Related DTO       : N/A
 * Related Mapper    : ComplianceProviderRegistryMapper
 * Related DB Table  : compliance_provider_registrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.tax.compliance;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Registry for compliance providers (ZATCA, PEPPOL, IRP, etc.).
 * Supports both auto-discovery of Spring-registered providers and
 * dynamic runtime registration for plugin-based extension.
 */
@Component
public class ComplianceProviderRegistry {

    private final Map<String, ComplianceProvider> providers;

    public ComplianceProviderRegistry(List<ComplianceProvider> complianceProviders) {
        this.providers = new ConcurrentHashMap<>(complianceProviders.stream()
                .collect(Collectors.toMap(
                        provider -> provider.getProviderType().toUpperCase(),
                        Function.identity()
                )));
    }

    /**
     * Retrieves provider data from the database.
     *
     * @param providerType the providerType input value
     * @return the ComplianceProvider result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public ComplianceProvider getProvider(String providerType) {
        ComplianceProvider provider = providers.get(providerType.toUpperCase());
        if (provider == null) {
            throw new IllegalArgumentException("Unsupported compliance provider: " + providerType);
        }
        return provider;
    }

    /**
     * Dynamically register a compliance provider at runtime.
     * This supports plugin-based e-invoicing format extensions (UBL 2.1, PEPPOL BIS, etc.).
     */
    public void registerProvider(ComplianceProvider provider) {
        providers.put(provider.getProviderType().toUpperCase(), provider);
    }

    /**
     * Check if a specific provider type is registered.
     */
    public boolean hasProvider(String providerType) {
        return providers.containsKey(providerType.toUpperCase());
    }

    /**
     * Returns all registered provider types.
     */
    public Collection<String> getRegisteredTypes() {
        return providers.keySet();
    }
}
