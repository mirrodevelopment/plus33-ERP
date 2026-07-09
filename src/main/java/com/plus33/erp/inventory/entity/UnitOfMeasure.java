/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.entity
 * File              : UnitOfMeasure.java
 * Purpose           : JPA Entity representing a persistent database record in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: UnitOfMeasureController
 * Related Service   : UnitOfMeasureService, UnitOfMeasureServiceImpl
 * Related Repository: UnitOfMeasureRepository
 * Related Entity    : UnitOfMeasure
 * Related DTO       : N/A
 * Related Mapper    : UnitOfMeasureMapper
 * Related DB Table  : units_of_measure
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : UnitOfMeasureRepository, UnitOfMeasureMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'units_of_measure'. Defines persistent domain object for Inventory Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code UnitOfMeasure}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'units_of_measure'.</p>
 *
 * <p><b>Database Table   :</b> {@code units_of_measure}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "units_of_measure")
@NoArgsConstructor
@AllArgsConstructor
public class UnitOfMeasure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false, length = 50)
    private String name;
}