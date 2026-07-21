/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * File              : Permission.java
 * Path              : src/main/java/com/plus33/erp/security/entity/Permission.java
 * Purpose           : JPA entity mapping the 'permissions' table — stores granular
 *                     action-level permission codes assigned to roles for fine-grained
 *                     API and UI access control throughout the ERP platform.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Granular permission entity within the PLUS33 Coffee ERP RBAC system.
 * Permissions are assigned to Roles via the role_permissions join table.
 * Each permission code becomes a GrantedAuthority string embedded in JWT
 * tokens by UserDetailsServiceImpl and checked by @PreAuthorize expressions
 * on controller endpoints.
 *
 * Columns:
 *   id          — auto-incremented primary key.
 *   code        — unique string (max 100 chars) identifying the permission
 *                 (e.g. "inventory.read", "payroll.approve", "reports.view").
 *                 Used directly as GrantedAuthority.getAuthority() value.
 *   name        — human-readable label (max 150 chars).
 *   description — freeform text for developer/admin documentation.
 *   createdAt   — set on insert, not updatable.
 *   roles       — inverse side of the ManyToMany relationship with Role
 *                 (mappedBy "permissions"). Not directly traversed in auth flow.
 *
 * Used by: Role (authority assignment), UserDetailsServiceImpl (authority
 *          flattening into JWT claims), @PreAuthorize method security.
 * Seeded via Flyway migrations. Not managed via API endpoints.
 ******************************************************************************/
package com.plus33.erp.security.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * <b>PLUS33 Coffee ERP -- Security Module</b>
 *
 * <p><b>Class  :</b> {@code Permission}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.security.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'permissions'.</p>
 *
 * <p><b>Database Table   :</b> {@code permissions}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();
}