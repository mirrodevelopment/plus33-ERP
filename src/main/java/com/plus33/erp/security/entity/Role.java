/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * Package           : com.plus33.erp.security.entity
 * File              : Role.java
 * Purpose           : JPA Entity representing a persistent database record in Security Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RoleController
 * Related Service   : RoleService, RoleServiceImpl
 * Related Repository: RoleRepository
 * Related Entity    : Role
 * Related DTO       : N/A
 * Related Mapper    : RoleMapper
 * Related DB Table  : roles
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : RoleRepository, RoleMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'roles'. Defines persistent domain object for Security Module with validation, relationship mappings, and lifecycle callbacks.
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