package com.plus33.erp.integration.eventmesh;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class KafkaBroker implements MessageBroker {
    private final Map<String, List<MessageListener>> subscribers = new ConcurrentHashMap<>();

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

    @Override
    public void subscribe(String topic, String consumerGroup, MessageListener listener) {
        subscribers.computeIfAbsent(topic, k -> new ArrayList<>()).add(listener);
    }
}