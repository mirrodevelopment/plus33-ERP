package com.plus33.erp.bi.insights;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AiInsightProviderRegistry {

    private final Map<String, AiInsightProvider> providers = new HashMap<>();

    @Autowired
    public AiInsightProviderRegistry(List<AiInsightProvider> providerList) {
        for (AiInsightProvider p : providerList) {
            providers.put(p.getDomain().toUpperCase(), p);
        }
    }

    public AiInsightProvider getProvider(String domain) {
        return providers.get(domain.toUpperCase());
    }
}