package com.plus33.erp.crm.ai;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AiPredictionRegistry {

    private final Map<String, AiPredictionProvider> providers = new HashMap<>();

    public AiPredictionRegistry(List<AiPredictionProvider> predictionProviders) {
        predictionProviders.forEach(p -> providers.put(p.getProviderName().toUpperCase(), p));
    }

    public AiPredictionProvider getProvider(String name) {
        return providers.get(name.toUpperCase());
    }
}
