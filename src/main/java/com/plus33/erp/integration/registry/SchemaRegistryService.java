/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.registry
 * File              : SchemaRegistryService.java
 * Purpose           : Business logic service layer for Integration Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SchemaRegistryController
 * Related Service   : SchemaRegistryService
 * Related Repository: SchemaRegistryRepository
 * Related Entity    : SchemaRegistry
 * Related DTO       : N/A
 * Related Mapper    : SchemaRegistryMapper
 * Related DB Table  : schema_registrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SchemaRegistryController, SchemaRegistryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Integration Module. Implements SchemaRegistryService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.integration.registry;

import com.plus33.erp.integration.entity.IntegrationSchemaRegistry;
import com.plus33.erp.integration.repository.IntegrationSchemaRegistryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code SchemaRegistryService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.registry}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Integration Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * SchemaRegistryController
 *   --> SchemaRegistryService (this)
 *   --> Validate business rules
 *   --> SchemaRegistryRepository (read/write 'schema_registrys')
 *   --> SchemaRegistryMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code schema_registrys}</p>
 * <p><b>Module Deps      :</b> Integration</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class SchemaRegistryService {
    @Autowired IntegrationSchemaRegistryRepository schemaRepo;
    /**
     * Creates a new schema and persists it to the database.
     *
     * @param eventType the eventType input value
     * @param version the version input value
     * @param definition the definition input value
     * @param rule the rule input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void registerSchema(String eventType, String version, String definition, String rule) {
        IntegrationSchemaRegistry sr = new IntegrationSchemaRegistry();
        sr.setEventType(eventType);
        sr.setSchemaVersion(version);
        sr.setSchemaDefinition(definition);
        sr.setCompatibilityRule(rule);
        sr.setCreatedAt(LocalDateTime.now());
        schemaRepo.save(sr);
    }

    /**
     * Validates business rules and constraints for event.
     *
     * @param eventType the eventType input value
     * @param version the version input value
     * @param payload the payload input value
     * @return true if operation succeeded, false otherwise
     * @throws BusinessException if a business rule is violated
     */
    public boolean validateEvent(String eventType, String version, String payload) {
        IntegrationSchemaRegistry sr = schemaRepo.findAll().stream()
                .filter(s -> s.getEventType().equals(eventType) && s.getSchemaVersion().equals(version))
                .findFirst().orElse(null);

        if (sr == null) {
            return false;
        }

        if (sr.getDeprecationDate() != null && LocalDateTime.now().isAfter(sr.getDeprecationDate())) {
            return false;
        }

        if (payload == null || payload.trim().isEmpty() || !payload.startsWith("{")) {
            return false;
        }

        return true;
    }
}