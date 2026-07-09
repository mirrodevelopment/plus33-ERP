/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.eventmesh
 * File              : InMemoryBroker.java
 * Purpose           : Component of Integration Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InMemoryBrokerController
 * Related Service   : InMemoryBrokerService, InMemoryBrokerServiceImpl
 * Related Repository: InMemoryBrokerRepository
 * Related Entity    : InMemoryBroker
 * Related DTO       : N/A
 * Related Mapper    : InMemoryBrokerMapper
 * Related DB Table  : in_memory_brokers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Integration Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Integration Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.integration.eventmesh;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code InMemoryBroker}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.eventmesh}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Integration Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class InMemoryBroker implements MessageBroker {
    private final Map<String, List<MessageListener>> subscribers = new ConcurrentHashMap<>();

    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     * @param topic the topic input value
     * @param event the event input value
     */
    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     * @param topic the topic input value
     * @param event the event input value
     */
    @Override
    public void publish(String topic, CloudEvent event) {
        List<MessageListener> listeners = subscribers.get(topic);
        if (listeners != null) {
            for (MessageListener listener : listeners) {
                try {
                    listener.onMessage(event);
                } catch (Exception e) {
                    System.err.println("Error processing message on topic " + topic + ": " + e.getMessage());
                }
            }
        }
    }

    /**
     * Performs the subscribe operation in this module.
     *
     * @param topic the topic input value
     * @param consumerGroup the consumerGroup input value
     * @param listener the listener input value
     */
    /**
     * Performs the subscribe operation in this module.
     *
     * @param topic the topic input value
     * @param consumerGroup the consumerGroup input value
     * @param listener the listener input value
     */
    @Override
    public void subscribe(String topic, String consumerGroup, MessageListener listener) {
        subscribers.computeIfAbsent(topic, k -> new ArrayList<>()).add(listener);
    }
}