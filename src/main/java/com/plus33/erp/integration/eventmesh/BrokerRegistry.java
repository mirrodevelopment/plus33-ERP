/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.eventmesh
 * File              : BrokerRegistry.java
 * Purpose           : Component of Integration Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BrokerRegistryController
 * Related Service   : BrokerRegistryService, BrokerRegistryServiceImpl
 * Related Repository: BrokerRegistryRepository
 * Related Entity    : BrokerRegistry
 * Related DTO       : N/A
 * Related Mapper    : BrokerRegistryMapper
 * Related DB Table  : broker_registrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Integration Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Integration Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.integration.eventmesh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code BrokerRegistry}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.eventmesh}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Integration Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class BrokerRegistry {
    @Value("${app.integration.broker-type:IN_MEMORY}")
    private String configuredBroker;

    /**
     * Retrieves active broker data from the database.
     *
     * @return the MessageBroker result
     * @throws ResourceNotFoundException if the entity is not found
     */
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