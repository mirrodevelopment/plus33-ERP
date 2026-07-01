package com.plus33.erp.crm.omnichannel;

public interface ChannelProvider {
    String getProviderKey();
    void syncCatalog();
    void processInboundMessage(String message);
}
