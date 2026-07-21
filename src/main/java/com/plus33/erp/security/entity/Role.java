/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * File              : Role.java
 * Path              : src/main/java/com/plus33/erp/security/entity/Role.java
 * Purpose           : JPA entity mapping the 'roles' table — defines named ERP
 *                     roles (e.g. ultimateAdmin, nationalAdmin, storeEmployee) each
 *                     carrying a set of granular Permission records for RBAC enforcement.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * RBAC (Role-Based Access Control) entity for PLUS33 Coffee ERP. Each User
 * is assigned one or more Roles via the user_roles join table. Each Role
 * grants a set of Permissions via role_permissions join table.
 *
 * Columns:
 *   id          — auto-incremented primary key.
 *   code        — unique string code identifying the role (e.g. "storeEmployee",
 *                 "nationalAdmin", "ultimateAdmin"). Used by UserDetailsServiceImpl
 *                 to build "ROLE_{code}" GrantedAuthority strings embedded in JWT.
 *   name        — human-readable label (max 100 chars).
 *   description — freeform text describing the role's organizational responsibility.
 *   createdAt   — set on insert, not updatable.
 *   permissions — LAZY ManyToMany to Permission via role_permissions join table.
 *                 Permission.code strings become individual GrantedAuthority entries
 *                 loaded by UserDetailsServiceImpl for fine-grained @PreAuthorize.
 *
 * Used by: UserDetailsServiceImpl (authority construction), User (assignment),
 *          RoleRepository (read-only queries for role code lookup).
 * Does not own business logic — pure RBAC data model.
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
 * <p><b>Class  :</b> {@code Role}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.security.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'roles'.</p>
 *
 * <p><b>Database Table   :</b> {@code roles}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "role_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<>();
}