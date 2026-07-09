/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.entity
 * File              : TaxGroupLine.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxGroupLineController
 * Related Service   : TaxGroupLineService, TaxGroupLineServiceImpl
 * Related Repository: TaxGroupLineRepository
 * Related Entity    : TaxGroupLine
 * Related DTO       : N/A
 * Related Mapper    : TaxGroupLineMapper
 * Related DB Table  : tax_group_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TaxGroupLineRepository, TaxGroupLineMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'tax_group_lines'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.tax.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxGroupLine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'tax_group_lines'.</p>
 *
 * <p><b>Database Table   :</b> {@code tax_group_lines}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "tax_group_lines")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxGroupLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private TaxGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rate_id", nullable = false)
    private TaxRate rate;
}