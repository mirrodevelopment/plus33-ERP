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
