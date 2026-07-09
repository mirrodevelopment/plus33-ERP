/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.entity
 * File              : FixedAssetReservation.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetReservationController
 * Related Service   : FixedAssetReservationService, FixedAssetReservationServiceImpl
 * Related Repository: FixedAssetReservationRepository
 * Related Entity    : FixedAssetReservation
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetReservationMapper
 * Related DB Table  : fixed_asset_reservations
 * Related REST APIs : N/A
 * Depends On        : Workforce Module
 * Used By           : FixedAssetReservationRepository, FixedAssetReservationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'fixed_asset_reservations'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.assets.entity;

import com.plus33.erp.workforce.entity.Employee;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetReservation}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'fixed_asset_reservations'.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_asset_reservations}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "fixed_asset_reservations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedAssetReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_asset_id", nullable = false)
    private FixedAsset fixedAsset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_employee_id", nullable = false)
    private Employee assignedEmployee;

    @Column(name = "reservation_date", nullable = false)
    private LocalDate reservationDate;

    @Column(name = "expected_checkout_date", nullable = false)
    private LocalDate expectedCheckoutDate;

    @Column(name = "expected_return_date", nullable = false)
    private LocalDate expectedReturnDate;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ReservationStatus status = ReservationStatus.REQUESTED;

    @Column(name = "checked_out_at")
    private LocalDateTime checkedOutAt;

    @Column(name = "returned_at")
    private LocalDateTime returnedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}