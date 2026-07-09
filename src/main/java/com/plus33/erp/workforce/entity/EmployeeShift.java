/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : EmployeeShift.java
 * Purpose           : JPA Entity representing a persistent database record in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeeShiftController
 * Related Service   : EmployeeShiftService, EmployeeShiftServiceImpl
 * Related Repository: EmployeeShiftRepository
 * Related Entity    : EmployeeShift
 * Related DTO       : N/A
 * Related Mapper    : EmployeeShiftMapper
 * Related DB Table  : employee_shifts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EmployeeShiftRepository, EmployeeShiftMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'employee_shifts'. Defines persistent domain object for Workforce Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code EmployeeShift}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'employee_shifts'.</p>
 *
 * <p><b>Database Table   :</b> {@code employee_shifts}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "employee_shifts")
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeShift {

    @EmbeddedId
    private EmployeeShiftId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("employeeId")
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("shiftId")
    @JoinColumn(name = "shift_id")
    private Shift shift;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class EmployeeShiftId implements Serializable {

        @Column(name = "employee_id")
        private Long employeeId;

        @Column(name = "shift_id")
        private Long shiftId;

        @Column(name = "effective_from")
        private LocalDate effectiveFrom;
    }
}