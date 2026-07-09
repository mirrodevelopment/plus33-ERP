/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.omnichannel
 * File              : ChannelProvider.java
 * Purpose           : Service interface contract defining the API for Crm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ChannelProviderController
 * Related Service   : ChannelProviderService, ChannelProviderServiceImpl
 * Related Repository: ChannelProviderRepository
 * Related Entity    : ChannelProvider
 * Related DTO       : N/A
 * Related Mapper    : ChannelProviderMapper
 * Related DB Table  : channel_providers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Crm Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Crm Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.crm.omnichannel;

public interface ChannelProvider {
    String getProviderKey();
    void syncCatalog();
    void processInboundMessage(String message);
}
