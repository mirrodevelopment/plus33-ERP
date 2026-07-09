/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.entity
 * File              : TaxCalendar.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxCalendarController
 * Related Service   : TaxCalendarService, TaxCalendarServiceImpl
 * Related Repository: TaxCalendarRepository
 * Related Entity    : TaxCalendar
 * Related DTO       : N/A
 * Related Mapper    : TaxCalendarMapper
 * Related DB Table  : tax_calendar
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : TaxCalendarRepository, TaxCalendarMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'tax_calendar'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.tax.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxCalendar}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'tax_calendar'.</p>
 *
 * <p><b>Database Table   :</b> {@code tax_calendar}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "tax_calendar")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "filing_type", nullable = false, length = 50)
    private String filingType; // VAT_RETURN, GST_RETURN, WHT_RETURN

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Builder.Default
    @Column(name = "status", nullable = false, length = 30)
    private String status = "DRAFT"; // DRAFT, CALCULATED, REVIEWED, APPROVED, SUBMITTED, ACCEPTED, REJECTED, AMENDED
}