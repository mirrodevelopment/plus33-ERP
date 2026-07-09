/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * Package           : com.plus33.erp.security.entity
 * File              : Permission.java
 * Purpose           : JPA Entity representing a persistent database record in Security Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PermissionController
 * Related Service   : PermissionService, PermissionServiceImpl
 * Related Repository: PermissionRepository
 * Related Entity    : Permission
 * Related DTO       : N/A
 * Related Mapper    : PermissionMapper
 * Related DB Table  : permissions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PermissionRepository, PermissionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'permissions'. Defines persistent domain object for Security Module with validation, relationship mappings, and lifecycle callbacks.
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