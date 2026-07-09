/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Intelligence Module
 * Package           : com.plus33.erp.intelligence.semantic
 * File              : TwinRelationshipService.java
 * Purpose           : Business logic service layer for Intelligence Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TwinRelationshipController
 * Related Service   : TwinRelationshipService
 * Related Repository: TwinRelationshipRepository
 * Related Entity    : TwinRelationship
 * Related DTO       : N/A
 * Related Mapper    : TwinRelationshipMapper
 * Related DB Table  : twin_relationships
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : TwinRelationshipController, TwinRelationshipServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Intelligence Module. Implements TwinRelationshipService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.intelligence.semantic;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Intelligence Module</b>
 *
 * <p><b>Class  :</b> {@code TwinRelationshipService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.intelligence.semantic}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Intelligence Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * TwinRelationshipController
 *   --> TwinRelationshipService (this)
 *   --> Validate business rules
 *   --> TwinRelationshipRepository (read/write 'twin_relationships')
 *   --> TwinRelationshipMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code twin_relationships}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class TwinRelationshipService {
    @Autowired PlatformTwinRelationRepository relationRepo;
    /**
     * Performs the relate operation in this module.
     *
     * @param src the src input value
     * @param target the target input value
     * @param type the type input value
     */
    @Transactional
    public void relate(Long src, Long target, String type) {
        PlatformTwinRelation rel = new PlatformTwinRelation();
        rel.setSourceInstanceId(src);
        rel.setTargetInstanceId(target);
        rel.setRelationshipType(type);
        rel.setUpdatedAt(LocalDateTime.now());
        relationRepo.save(rel);
    }
}