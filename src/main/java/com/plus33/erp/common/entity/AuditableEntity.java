/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Common Module
 * File              : AuditableEntity.java
 * Path              : src/main/java/com/plus33/erp/common/entity/AuditableEntity.java
 * Purpose           : Abstract JPA @MappedSuperclass providing automatic audit trail
 *                     columns (createdAt, updatedAt, createdBy, updatedBy) inherited
 *                     by all auditable domain entities across the ERP platform.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Base class for JPA entities that require full audit trail tracking.
 * Annotated with @MappedSuperclass so JPA includes its columns in every
 * subclass table. Uses Spring Data JPA's AuditingEntityListener for
 * automatic population without explicit setter calls.
 *
 * Audit columns:
 *   created_at  — timestamped automatically on first persist via @CreatedDate.
 *                 marked updatable = false to prevent modification after insert.
 *   updated_at  — refreshed automatically on every update via @LastModifiedDate.
 *   created_by  — Long user ID populated via @CreatedBy from
 *                 SecurityAuditorAware. Non-updatable after first insert.
 *   updated_by  — Long user ID populated via @LastModifiedBy from
 *                 SecurityAuditorAware on every save.
 *
 * SecurityAuditorAware (common.audit package) resolves the currently
 * authenticated user's ID from the Spring SecurityContextHolder to
 * populate createdBy/updatedBy automatically.
 *
 * JPA auditing must be enabled via @EnableJpaAuditing (JpaAuditingConfig).
 *
 * Extended by: Employee, Store, Region, Nation, SalaryComponent, PurchaseOrder,
 *              and most other domain entities across all ERP modules.
 ******************************************************************************/
package com.plus33.erp.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
/**
 * <b>PLUS33 Coffee ERP -- Common Module</b>
 *
 * <p><b>Class  :</b> {@code AuditableEntity}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.common.entity}</p>
 * <p><b>Layer  :</b> Component of Common Module in the PLUS33 Coffee ERP platform.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public abstract class AuditableEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;
}
