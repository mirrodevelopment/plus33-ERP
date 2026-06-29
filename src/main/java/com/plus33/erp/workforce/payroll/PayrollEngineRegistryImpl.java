package com.plus33.erp.workforce.payroll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;

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

    @Override
    public void registerProvider(PayrollEngineProvider provider) {
        if (provider != null && provider.getCountryCode() != null) {
            providers.put(provider.getCountryCode().toUpperCase(), provider);
        }
    }

    @Override
    public PayrollEngineProvider getProvider(String countryCode) {
        if (countryCode == null) {
            return providers.get("US");
        }
        PayrollEngineProvider provider = providers.get(countryCode.toUpperCase());
        return provider != null ? provider : providers.get("US");
    }
}
