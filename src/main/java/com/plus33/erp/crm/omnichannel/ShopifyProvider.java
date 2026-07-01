package com.plus33.erp.crm.omnichannel;

import org.springframework.stereotype.Service;

@Service
public class ShopifyProvider implements ChannelProvider {

    @Override
    public String getProviderKey() {
        return "SHOPIFY";
    }

    @Override
    public void syncCatalog() {}

    @Override
    public void processInboundMessage(String message) {}
}
