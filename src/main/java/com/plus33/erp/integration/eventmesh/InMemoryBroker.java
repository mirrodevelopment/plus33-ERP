package com.plus33.erp.integration.eventmesh;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryBroker implements MessageBroker {
    private final Map<String, List<MessageListener>> subscribers = new ConcurrentHashMap<>();

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

    @Override
    public void subscribe(String topic, String consumerGroup, MessageListener listener) {
        subscribers.computeIfAbsent(topic, k -> new ArrayList<>()).add(listener);
    }
}