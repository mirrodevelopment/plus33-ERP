/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.entity
 * File              : Store.java
 * Purpose           : JPA Entity representing a persistent database record in Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StoreController
 * Related Service   : StoreService, StoreServiceImpl
 * Related Repository: StoreRepository
 * Related Entity    : Store
 * Related DTO       : N/A
 * Related Mapper    : StoreMapper
 * Related DB Table  : stores
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : StoreRepository, StoreMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'stores'. Defines persistent domain object for Organization Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.organization.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Organization Module</b>
 *
 * <p><b>Class  :</b> {@code Store}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'stores'.</p>
 *
 * <p><b>Database Table   :</b> {@code stores}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String address;

    @Column(length = 30)
    private String phone;

    @Column(length = 150)
    private String email;

    @Column(length = 100)
    private String timezone;

    @Column(name = "opening_date")
    private LocalDate openingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column(nullable = false)
    private Boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StoreType type = StoreType.COMPACT_CAFE;

    /** GPS latitude – set once by Store Admin from their device; persists until next explicit update. */
    @Column(name = "latitude", precision = 10, scale = 8)
    private java.math.BigDecimal latitude;

    /** GPS longitude – set once by Store Admin from their device; persists until next explicit update. */
    @Column(name = "longitude", precision = 11, scale = 8)
    private java.math.BigDecimal longitude;

    /**
     * Geofence radius in metres.
     * Clock-in requires employee within 30 m; auto clock-out triggers beyond this value (200 m default).
     */
    @Column(name = "geofence_radius_meters")
    private Integer geofenceRadiusMeters = 200;

    @Column(name = "operating_hours", nullable = false, length = 100)
    private String operatingHours = "08:00 - 22:00";

    @Column(name = "wifi_ssid", nullable = false, length = 100)
    private String wifiSsid = "PLUS33-Guest";

    @Column(name = "wifi_password", nullable = false, length = 100)
    private String wifiPassword = "CoffeeBreak";

    @Column(name = "low_stock_threshold", nullable = false)
    private Integer lowStockThreshold = 50;

    @Column(name = "sales_target", nullable = false, precision = 12, scale = 2)
    private java.math.BigDecimal salesTarget = new java.math.BigDecimal("10000.00");

    @Column(name = "receipt_footer", nullable = false, columnDefinition = "TEXT")
    private String receiptFooter = "Thank you for visiting PLUS33 Coffee!";

    @Column(name = "admin_name", nullable = false, length = 100)
    private String adminName = "giri";

    @Column(name = "admin_number", nullable = false, length = 50)
    private String adminNumber = "EMP10245";

    @Column(name = "admin_mobile", nullable = false, length = 30)
    private String adminMobile = "+919999999999";

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<StoreDocument> documents = new java.util.ArrayList<>();

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