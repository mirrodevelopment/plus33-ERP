/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.entity
 * File              : FiscalYear.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FiscalYearController
 * Related Service   : FiscalYearService, FiscalYearServiceImpl
 * Related Repository: FiscalYearRepository
 * Related Entity    : FiscalYear
 * Related DTO       : N/A
 * Related Mapper    : FiscalYearMapper
 * Related DB Table  : fiscal_years
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : FiscalYearRepository, FiscalYearMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'fiscal_years'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.reporting.entity;

import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.finance.entity.JournalEntry;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "fiscal_years", uniqueConstraints = {
    @UniqueConstraint(name = "uk_fiscal_year_company", columnNames = {"company_id", "fiscal_year"})
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FiscalYear}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.reporting.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'fiscal_years'.</p>
 *
 * <p><b>Database Table   :</b> {@code fiscal_years}</p>
 * <p><b>Module Deps      :</b> Organization, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FiscalYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "fiscal_year", nullable = false)
    private Integer fiscalYear;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private FiscalYearStatus status = FiscalYearStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opening_journal_id")
    private JournalEntry openingJournal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "closing_journal_id")
    private JournalEntry closingJournal;

    @Column(name = "closed_by", length = 100)
    private String closedBy;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;
}