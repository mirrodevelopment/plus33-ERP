/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : UserStore.java
 * Purpose           : JPA Entity representing a persistent database record in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: UserStoreController
 * Related Service   : UserStoreService, UserStoreServiceImpl
 * Related Repository: UserStoreRepository
 * Related Entity    : UserStore
 * Related DTO       : N/A
 * Related Mapper    : UserStoreMapper
 * Related DB Table  : user_stores
 * Related REST APIs : N/A
 * Depends On        : Organization Module, Security Module
 * Used By           : UserStoreRepository, UserStoreMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'user_stores'. Defines persistent domain object for Workforce Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code UserStore}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'user_stores'.</p>
 *
 * <p><b>Database Table   :</b> {@code user_stores}</p>
 * <p><b>Module Deps      :</b> Organization, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "user_stores")
@NoArgsConstructor
@AllArgsConstructor
public class UserStore {

    @EmbeddedId
    private UserStoreId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("storeId")
    @JoinColumn(name = "store_id")
    private Store store;

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
    public static class UserStoreId implements Serializable {
        @Column(name = "user_id")
        private Long userId;

        @Column(name = "store_id")
        private Long storeId;
    }
}