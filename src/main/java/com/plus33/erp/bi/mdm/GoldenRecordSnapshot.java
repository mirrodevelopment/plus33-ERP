/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.mdm
 * File              : GoldenRecordSnapshot.java
 * Purpose           : Component of Bi Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GoldenRecordSnapshotController
 * Related Service   : GoldenRecordSnapshotService, GoldenRecordSnapshotServiceImpl
 * Related Repository: GoldenRecordSnapshotRepository
 * Related Entity    : GoldenRecordSnapshot
 * Related DTO       : N/A
 * Related Mapper    : GoldenRecordSnapshotMapper
 * Related DB Table  : golden_record_snapshots
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Bi Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Bi Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.bi.mdm;

import java.util.HashMap;
import java.util.Map;

public class GoldenRecordSnapshot {
    private Map<String, String> attributes = new HashMap<>();
    
    /**
     * Performs the setAttribute operation in this module.
     *
     * @param name the name input value
     * @param value the value input value
     */
    public void setAttribute(String name, String value) {
        attributes.put(name, value);
    }
    
    /**
     * Retrieves attribute data from the database.
     *
     * @param name the name input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAttribute(String name) {
        return attributes.get(name);
    }
    
    /**
     * Retrieves attributes data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }
}