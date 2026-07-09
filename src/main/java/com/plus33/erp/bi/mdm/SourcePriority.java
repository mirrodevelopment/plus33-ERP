/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.mdm
 * File              : SourcePriority.java
 * Purpose           : Component of Bi Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SourcePriorityController
 * Related Service   : SourcePriorityService, SourcePriorityServiceImpl
 * Related Repository: SourcePriorityRepository
 * Related Entity    : SourcePriority
 * Related DTO       : N/A
 * Related Mapper    : SourcePriorityMapper
 * Related DB Table  : source_prioritys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Bi Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Bi Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.bi.mdm;

public class SourcePriority {
    private String sourceSystem;
    private int priority;
    
    public SourcePriority(String sourceSystem, int priority) {
        this.sourceSystem = sourceSystem;
        this.priority = priority;
    }
    
    /**
     * Retrieves source system data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSourceSystem() { return sourceSystem; }
    /**
     * Retrieves priority data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getPriority() { return priority; }
}