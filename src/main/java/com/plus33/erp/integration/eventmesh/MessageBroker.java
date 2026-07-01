package com.plus33.erp.integration.eventmesh;

public interface MessageBroker {
    void publish(String topic, CloudEvent event);
    void subscribe(String topic, String consumerGroup, MessageListener listener);
}