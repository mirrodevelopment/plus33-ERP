/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.entity
 * File              : CreditNoteStatus.java
 * Purpose           : Enumeration of typed constants for Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CreditNoteStatusController
 * Related Service   : CreditNoteStatusService, CreditNoteStatusServiceImpl
 * Related Repository: CreditNoteStatusRepository
 * Related Entity    : CreditNoteStatus
 * Related DTO       : N/A
 * Related Mapper    : CreditNoteStatusMapper
 * Related DB Table  : credit_note_statuss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Sales Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Sales Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.sales.entity;

public enum CreditNoteStatus {
    DRAFT,
    APPROVED,
    POSTED,
    CANCELLED
}
