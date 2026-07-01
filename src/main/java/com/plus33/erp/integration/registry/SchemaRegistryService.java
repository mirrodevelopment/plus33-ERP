package com.plus33.erp.integration.registry;

import com.plus33.erp.integration.entity.IntegrationSchemaRegistry;
import com.plus33.erp.integration.repository.IntegrationSchemaRegistryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class SchemaRegistryService {
    @Autowired IntegrationSchemaRegistryRepository schemaRepo;

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
