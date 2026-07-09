/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : UserWarehouse.java
 * Purpose           : JPA Entity representing a persistent database record in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: UserWarehouseController
 * Related Service   : UserWarehouseService, UserWarehouseServiceImpl
 * Related Repository: UserWarehouseRepository
 * Related Entity    : UserWarehouse
 * Related DTO       : N/A
 * Related Mapper    : UserWarehouseMapper
 * Related DB Table  : user_warehouses
 * Related REST APIs : N/A
 * Depends On        : Organization Module, Security Module
 * Used By           : UserWarehouseRepository, UserWarehouseMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'user_warehouses'. Defines persistent domain object for Workforce Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code UserWarehouse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'user_warehouses'.</p>
 *
 * <p><b>Database Table   :</b> {@code user_warehouses}</p>
 * <p><b>Module Deps      :</b> Organization, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "user_warehouses")
@NoArgsConstructor
@AllArgsConstructor
public class UserWarehouse {

    @EmbeddedId
    private UserWarehouseId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("warehouseId")
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column(name = "assigned_at", nullable = false, updatable = false)
    private LocalDateTime assignedAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        assignedAt = LocalDateTime.now();
    }

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class UserWarehouseId implements Serializable {
        @Column(name = "user_id")
        private Long userId;

        @Column(name = "warehouse_id")
        private Long warehouseId;
    }
}