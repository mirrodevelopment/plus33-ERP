/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.entity
 * File              : DepreciationBook.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DepreciationBookController
 * Related Service   : DepreciationBookService, DepreciationBookServiceImpl
 * Related Repository: DepreciationBookRepository
 * Related Entity    : DepreciationBook
 * Related DTO       : N/A
 * Related Mapper    : DepreciationBookMapper
 * Related DB Table  : depreciation_books
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : DepreciationBookRepository, DepreciationBookMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'depreciation_books'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.assets.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "depreciation_books", uniqueConstraints = {
    @UniqueConstraint(name = "uk_depr_books_company_code", columnNames = {"company_id", "code"})
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code DepreciationBook}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'depreciation_books'.</p>
 *
 * <p><b>Database Table   :</b> {@code depreciation_books}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepreciationBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false, length = 30)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;
}