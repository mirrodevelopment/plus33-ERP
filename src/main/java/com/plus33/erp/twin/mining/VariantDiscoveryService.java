/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Twin Module
 * Package           : com.plus33.erp.twin.mining
 * File              : VariantDiscoveryService.java
 * Purpose           : Business logic service layer for Twin Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: VariantDiscoveryController
 * Related Service   : VariantDiscoveryService
 * Related Repository: VariantDiscoveryRepository
 * Related Entity    : VariantDiscovery
 * Related DTO       : N/A
 * Related Mapper    : VariantDiscoveryMapper
 * Related DB Table  : variant_discoverys
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : VariantDiscoveryController, VariantDiscoveryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Twin Module. Implements VariantDiscoveryService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.twin.mining;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Twin Module</b>
 *
 * <p><b>Class  :</b> {@code VariantDiscoveryService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.twin.mining}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Twin Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * VariantDiscoveryController
 *   --> VariantDiscoveryService (this)
 *   --> Validate business rules
 *   --> VariantDiscoveryRepository (read/write 'variant_discoverys')
 *   --> VariantDiscoveryMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code variant_discoverys}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class VariantDiscoveryService {
    /**
     * Performs the discoverVariant operation in this module.
     *
     * @param caseId the caseId input value
     * @return List of matching records
     */
    @Autowired PlatformProcessEventLogRepository eventLogRepo;
    public List<PlatformProcessEventLog> discoverVariant(Long caseId) {
        return eventLogRepo.findAll().stream()
                .filter(e -> e.getCaseId().equals(caseId))
                .toList();
    }
}