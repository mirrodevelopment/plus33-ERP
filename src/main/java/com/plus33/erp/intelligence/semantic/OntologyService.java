/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Intelligence Module
 * Package           : com.plus33.erp.intelligence.semantic
 * File              : OntologyService.java
 * Purpose           : Business logic service layer for Intelligence Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: OntologyController
 * Related Service   : OntologyService
 * Related Repository: OntologyRepository
 * Related Entity    : Ontology
 * Related DTO       : N/A
 * Related Mapper    : OntologyMapper
 * Related DB Table  : ontologys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : OntologyController, OntologyServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Intelligence Module. Implements OntologyService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.intelligence.semantic;

import org.springframework.stereotype.Service;

@Service
public class OntologyService {
    /**
     * Validates business rules and constraints for relationship.
     *
     * @param type the type input value
     * @return true if operation succeeded, false otherwise
     * @throws BusinessException if a business rule is violated
     */
    public boolean validateRelationship(String type) {
        return "DependsOn".equals(type) || "LocatedIn".equals(type) || "ProducedBy".equals(type) ||
               "Consumes".equals(type) || "Controls".equals(type) || "DerivedFrom".equals(type) || "OwnedBy".equals(type);
    }
}