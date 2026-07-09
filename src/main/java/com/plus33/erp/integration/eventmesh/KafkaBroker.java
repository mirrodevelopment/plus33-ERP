/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.eventmesh
 * File              : KafkaBroker.java
 * Purpose           : Component of Integration Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: KafkaBrokerController
 * Related Service   : KafkaBrokerService, KafkaBrokerServiceImpl
 * Related Repository: KafkaBrokerRepository
 * Related Entity    : KafkaBroker
 * Related DTO       : N/A
 * Related Mapper    : KafkaBrokerMapper
 * Related DB Table  : kafka_brokers
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
 * <p><b>Class  :</b> {@code KafkaBroker}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.eventmesh}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Integration Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class KafkaBroker implements MessageBroker {
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
        System.out.println("KafkaBroker [Publishing to topic: " + topic + ", payload ID: " + event.getId() + "]");
        List<MessageListener> listeners = subscribers.get(topic);
        if (listeners != null) {
            for (MessageListener listener : listeners) {
                listener.onMessage(event);
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