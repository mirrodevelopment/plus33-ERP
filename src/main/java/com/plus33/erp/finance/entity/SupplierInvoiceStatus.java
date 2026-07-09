/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.entity
 * File              : SupplierInvoiceStatus.java
 * Purpose           : Enumeration of typed constants for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierInvoiceStatusController
 * Related Service   : SupplierInvoiceStatusService, SupplierInvoiceStatusServiceImpl
 * Related Repository: SupplierInvoiceStatusRepository
 * Related Entity    : SupplierInvoiceStatus
 * Related DTO       : N/A
 * Related Mapper    : SupplierInvoiceStatusMapper
 * Related DB Table  : supplier_invoice_statuss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Finance Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.finance.entity;

public enum SupplierInvoiceStatus {
    DRAFT,
    SUBMITTED,
    APPROVED,
    PARTIALLY_PAID,
    PAID,
    CANCELLED,
    VOID
}
