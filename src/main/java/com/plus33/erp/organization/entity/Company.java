/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.entity
 * File              : Company.java
 * Purpose           : JPA Entity representing a persistent database record in Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CompanyController
 * Related Service   : CompanyService, CompanyServiceImpl
 * Related Repository: CompanyRepository
 * Related Entity    : Company
 * Related DTO       : N/A
 * Related Mapper    : CompanyMapper
 * Related DB Table  : companies
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CompanyRepository, CompanyMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'companies'. Defines persistent domain object for Organization Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.organization.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Organization Module</b>
 *
 * <p><b>Class  :</b> {@code Company}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'companies'.</p>
 *
 * <p><b>Database Table   :</b> {@code companies}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "legal_name", length = 255)
    private String legalName;

    @Column(name = "country_code", length = 10)
    private String countryCode;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "fiscal_year_start_month", nullable = false)
    private Integer fiscalYearStartMonth = 1;

    @Column(name = "fiscal_year_start_day", nullable = false)
    private Integer fiscalYearStartDay = 1;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}