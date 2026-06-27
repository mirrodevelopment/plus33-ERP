package com.plus33.erp.finance.tax.compliance;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ComplianceProviderRegistry {

    private final Map<String, ComplianceProvider> providers;

    public ComplianceProviderRegistry(List<ComplianceProvider> complianceProviders) {
        this.providers = complianceProviders.stream()
                .collect(Collectors.toMap(
                        provider -> provider.getProviderType().toUpperCase(),
                        Function.identity()
                ));
    }

    public ComplianceProvider getProvider(String providerType) {
        ComplianceProvider provider = providers.get(providerType.toUpperCase());
        if (provider == null) {
            throw new IllegalArgumentException("Unsupported compliance provider: " + providerType);
        }
        return provider;
    }
}
