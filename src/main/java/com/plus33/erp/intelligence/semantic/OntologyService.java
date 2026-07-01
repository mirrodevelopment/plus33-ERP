package com.plus33.erp.intelligence.semantic;

import org.springframework.stereotype.Service;

@Service
public class OntologyService {
    public boolean validateRelationship(String type) {
        return "DependsOn".equals(type) || "LocatedIn".equals(type) || "ProducedBy".equals(type) ||
               "Consumes".equals(type) || "Controls".equals(type) || "DerivedFrom".equals(type) || "OwnedBy".equals(type);
    }
}