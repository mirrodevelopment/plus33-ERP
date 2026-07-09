/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.eventmesh
 * File              : MessageListener.java
 * Purpose           : Service interface contract defining the API for Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MessageListenerController
 * Related Service   : MessageListenerService, MessageListenerServiceImpl
 * Related Repository: MessageListenerRepository
 * Related Entity    : MessageListener
 * Related DTO       : N/A
 * Related Mapper    : MessageListenerMapper
 * Related DB Table  : message_listeners
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Integration Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Integration Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.integration.eventmesh;

public interface MessageListener {
    void onMessage(CloudEvent event);
}