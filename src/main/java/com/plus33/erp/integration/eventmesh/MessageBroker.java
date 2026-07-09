/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.eventmesh
 * File              : MessageBroker.java
 * Purpose           : Service interface contract defining the API for Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MessageBrokerController
 * Related Service   : MessageBrokerService, MessageBrokerServiceImpl
 * Related Repository: MessageBrokerRepository
 * Related Entity    : MessageBroker
 * Related DTO       : N/A
 * Related Mapper    : MessageBrokerMapper
 * Related DB Table  : message_brokers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Integration Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Integration Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.integration.eventmesh;

public interface MessageBroker {
    void publish(String topic, CloudEvent event);
    void subscribe(String topic, String consumerGroup, MessageListener listener);
}