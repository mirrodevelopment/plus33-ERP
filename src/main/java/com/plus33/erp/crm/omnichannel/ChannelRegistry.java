package com.plus33.erp.crm.omnichannel;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChannelRegistry {

    private final Map<String, ChannelProvider> providers = new HashMap<>();

    public ChannelRegistry(List<ChannelProvider> channelProviders) {
        channelProviders.forEach(p -> providers.put(p.getProviderKey().toUpperCase(), p));
    }

    public ChannelProvider getProvider(String key) {
        return providers.get(key.toUpperCase());
    }
}
