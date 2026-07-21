/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * File              : User.java
 * Path              : src/main/java/com/plus33/erp/security/entity/User.java
 * Purpose           : JPA entity mapping the 'users' table — stores login credentials,
 *                     display names, activation state, avatar URL, and role assignments
 *                     for every ERP platform user across all roles.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Core identity entity for PLUS33 Coffee ERP. Every person who can log into
 * the system has a User record. Mapped to the PostgreSQL 'users' table.
 *
 * Columns:
 *   id          — auto-incremented primary key (IDENTITY strategy).
 *   email       — unique, non-null. Used as the authentication principal
 *                 (username) for JWT token subject and UserDetailsServiceImpl.
 *   password    — BCrypt-encoded, non-null. Verified by PasswordEncoder in
 *                 DaoAuthenticationProvider during login.
 *   firstName   — non-null, max 100 chars. Shown in profile pages and
 *                 returned by GET /api/v1/auth/me.
 *   lastName    — nullable, max 100 chars.
 *   active      — boolean flag controlling whether the user can authenticate.
 *                 Mapped to UserDetails.disabled() in UserDetailsServiceImpl.
 *   createdAt   — set on @PrePersist, not updatable.
 *   updatedAt   — refreshed on @PreUpdate.
 *   avatarUrl   — nullable, max 500 chars. Stores relative path to uploaded
 *                 or default avatar image served to the frontend profile pages.
 *   roles       — ManyToMany relationship via user_roles join table.
 *                 Each Role contains a code and a set of Permissions. Loaded
 *                 LAZILY. Flattened into GrantedAuthority list by
 *                 UserDetailsServiceImpl for JWT claims and @PreAuthorize.
 *
 * Used by: AuthController (profile retrieval/update), UserDetailsServiceImpl
 *          (credential loading), UserRepository (persistence operations).
 * Does not store employee-specific payroll or HR data (see Employee entity).
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
 * <p><b>Class  :</b> {@code User}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.security.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'users'.</p>
 *
 * <p><b>Database Table   :</b> {@code users}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}