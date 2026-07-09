/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.mdm
 * File              : AttributeResolutionRule.java
 * Purpose           : Enumeration of typed constants for Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AttributeResolutionRuleController
 * Related Service   : AttributeResolutionRuleService, AttributeResolutionRuleServiceImpl
 * Related Repository: AttributeResolutionRuleRepository
 * Related Entity    : AttributeResolutionRule
 * Related DTO       : N/A
 * Related Mapper    : AttributeResolutionRuleMapper
 * Related DB Table  : attribute_resolution_rules
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Bi Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Bi Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.bi.mdm;

public enum AttributeResolutionRule {
    HIGHEST_CONFIDENCE,
    MOST_RECENT,
    TRUSTED_SOURCE_PRIORITY,
    LONGEST_HISTORY,
    MANUAL_OVERRIDE
}