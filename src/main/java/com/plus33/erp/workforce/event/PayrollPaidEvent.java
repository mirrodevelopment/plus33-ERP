/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.event
 * File              : PayrollPaidEvent.java
 * Purpose           : Spring ApplicationEvent published by Workforce Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollPaidEventController
 * Related Service   : PayrollPaidEventService, PayrollPaidEventServiceImpl
 * Related Repository: PayrollPaidEventRepository
 * Related Entity    : PayrollPaidEvent
 * Related DTO       : N/A
 * Related Mapper    : PayrollPaidEventMapper
 * Related DB Table  : payroll_paid_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Workforce Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Spring ApplicationEvent for Workforce Module. Published on state changes. Consumed by @EventListener methods for async processing.
 ******************************************************************************/
package com.plus33.erp.workforce.event;

public class PayrollPaidEvent {
    private final Long payrollRunId;
    private final Long companyId;

    public PayrollPaidEvent(Long payrollRunId, Long companyId) {
        this.payrollRunId = payrollRunId;
        this.companyId = companyId;
    }

    /**
     * Retrieves payroll run id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPayrollRunId() { return payrollRunId; }
    /**
     * Retrieves company id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; }
}
