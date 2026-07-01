package com.plus33.erp.integration.eventmesh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BrokerRegistry {
    @Value("${app.integration.broker-type:IN_MEMORY}")
    private String configuredBroker;

    @Autowired InMemoryBroker inMemoryBroker;
    @Autowired KafkaBroker kafkaBroker;
    @Autowired RabbitMqBroker rabbitMqBroker;

    public MessageBroker getActiveBroker() {
        if ("KAFKA".equalsIgnoreCase(configuredBroker)) {
            return kafkaBroker;
        } else if ("RABBITMQ".equalsIgnoreCase(configuredBroker)) {
            return rabbitMqBroker;
        }
        return inMemoryBroker;
    }
}