/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * Package           : com.plus33.erp.security.repository
 * File              : PermissionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Security Module entities
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
 * Used By           : PermissionService, PermissionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Security Module against the 'permissions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.security.repository;

import com.plus33.erp.security.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Security Module</b>
 *
 * <p><b>Class  :</b> {@code PermissionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.security.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'permissions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code permissions}</p>
 * <p><b>Module Deps      :</b> Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByCode(String code);

    boolean existsByCode(String code);
}
