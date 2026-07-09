/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * Package           : com.plus33.erp.security.repository
 * File              : RoleRepository.java
 * Purpose           : JPA Repository providing database CRUD for Security Module entities
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
 * Used By           : RoleService, RoleServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Security Module against the 'roles' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.security.repository;

import com.plus33.erp.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Security Module</b>
 *
 * <p><b>Class  :</b> {@code RoleRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.security.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'roles' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code roles}</p>
 * <p><b>Module Deps      :</b> Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByCode(String code);

    boolean existsByCode(String code);
}
