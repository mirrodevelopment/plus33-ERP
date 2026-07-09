/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.service
 * File              : ResourceCapacityEngine.java
 * Purpose           : Business logic service layer for Project Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ResourceCapacityEngineController
 * Related Service   : ResourceCapacityEngine
 * Related Repository: ResourceAssignmentRepository
 * Related Entity    : ResourceCapacityEngine
 * Related DTO       : N/A
 * Related Mapper    : ResourceCapacityEngineMapper
 * Related DB Table  : resource_capacity_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ResourceCapacityEngineController, ResourceCapacityEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Project Module. Implements ResourceCapacityEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.project.service;

import com.plus33.erp.project.entity.ResourceAssignment;
import com.plus33.erp.project.repository.ResourceAssignmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ResourceCapacityEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Project Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ResourceCapacityEngineController
 *   --> ResourceCapacityEngine (this)
 *   --> Validate business rules
 *   --> ResourceCapacityEngineRepository (read/write 'resource_capacity_engines')
 *   --> ResourceCapacityEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code resource_capacity_engines}</p>
 * <p><b>Module Deps      :</b> Project</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ResourceCapacityEngine {

    private final ResourceAssignmentRepository assignmentRepository;

    public ResourceCapacityEngine(ResourceAssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    /**
     * Validates business rules and constraints for over allocation.
     *
     * @param resourceId the resourceId input value
     * @return true if operation succeeded, false otherwise
     * @throws BusinessException if a business rule is violated
     */
    @Transactional(readOnly = true)
    public boolean checkOverAllocation(Long resourceId) {
        List<ResourceAssignment> assignments = assignmentRepository.findByResourceId(resourceId);
        BigDecimal totalAllocation = BigDecimal.ZERO;
        for (ResourceAssignment a : assignments) {
            totalAllocation = totalAllocation.add(a.getAllocationPercentage());
        }
        return totalAllocation.compareTo(new BigDecimal("100.00")) > 0;
    }
}