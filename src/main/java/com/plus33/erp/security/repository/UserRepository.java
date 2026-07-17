/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * Package           : com.plus33.erp.security.repository
 * File              : UserRepository.java
 * Purpose           : JPA Repository providing database CRUD for Security Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: UserController
 * Related Service   : UserService, UserServiceImpl
 * Related Repository: UserRepository
 * Related Entity    : User
 * Related DTO       : N/A
 * Related Mapper    : UserMapper
 * Related DB Table  : users
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : UserService, UserServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Security Module against the 'users' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.security.repository;

import com.plus33.erp.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Security Module</b>
 *
 * <p><b>Class  :</b> {@code UserRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.security.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'users' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code users}</p>
 * <p><b>Module Deps      :</b> Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @org.springframework.data.jpa.repository.Query("SELECT u FROM User u WHERE " +
        "LOWER(TRIM(CONCAT(COALESCE(u.firstName, ''), ' ', COALESCE(u.lastName, '')))) = LOWER(:fullName)")
    java.util.List<User> findByFullName(@org.springframework.data.repository.query.Param("fullName") String fullName);
}
