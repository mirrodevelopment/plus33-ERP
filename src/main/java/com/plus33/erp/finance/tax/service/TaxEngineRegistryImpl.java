package com.plus33.erp.finance.tax.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TaxEngineRegistryImpl implements TaxEngineRegistry {

    private final Map<String, TaxEngineProvider> registry = new ConcurrentHashMap<>();

    public TaxEngineRegistryImpl(List<TaxEngineProvider> providers) {
        for (TaxEngineProvider provider : providers) {
            register(provider.getTaxType(), provider);
        }
    }

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

    @Override
    public void register(String taxCategoryCode, TaxEngineProvider provider) {
        registry.put(taxCategoryCode.toUpperCase(), provider);
    }
}
