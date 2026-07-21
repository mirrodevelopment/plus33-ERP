/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * File              : UserDetailsServiceImpl.java
 * Path              : src/main/java/com/plus33/erp/security/service/UserDetailsServiceImpl.java
 * Purpose           : Loads user credentials and granted authorities from the database
 *                     to fulfil Spring Security's authentication contract during login
 *                     and permission-based access control across all ERP endpoints.
 * Version           : 1.0.0
 *
 * Description
 * ---------------------------------------------------------------------------
 * Spring @Service implementing UserDetailsService — the mandatory contract
 * that Spring Security's DaoAuthenticationProvider calls during login to
 * resolve a user principal by identifier (email in this ERP).
 *
 * loadUserByUsername(email):
 *   1. Looks up the User entity by email via UserRepository.
 *      Throws UsernameNotFoundException if not found (triggers 401).
 *   2. Builds a Set<GrantedAuthority> by flattening each assigned Role:
 *        - Adds "ROLE_{role.code}" as a SimpleGrantedAuthority (e.g.
 *          ROLE_storeEmployee, ROLE_nationalAdmin, ROLE_ultimateAdmin).
 *        - Adds each Permission.code from the role's permission set
 *          as individual GrantedAuthority entries for @PreAuthorize checks.
 *   3. Returns a Spring Security User built from email, BCrypt password,
 *      authority set, and the user's active flag (disabled if false).
 *
 * This method is called during:
 *   a) Login flow — DaoAuthenticationProvider calls it to load and verify
 *      credentials, then passes principal to JwtService.generateToken().
 *   b) Token generation — UserDetails authorities are embedded as JWT claims.
 *
 * Does NOT handle session management (stateless architecture).
 * Does NOT own password encoding — encoding is done in AuthController.
 *
 * Dependencies:
 *   - UserRepository (security.repository) — user + roles + permissions load
 *   - Role, Permission (security.entity) — RBAC hierarchy
 ******************************************************************************/
package com.plus33.erp.security.service;

import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <b>PLUS33 Coffee ERP -- Security Module</b>
 *
 * <p><b>Class  :</b> {@code UserDetailsServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.security.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Security Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * UserDetailsController
 *   --> UserDetailsServiceImpl (this)
 *   --> Validate business rules
 *   --> UserDetailsRepository (read/write 'user_detailss')
 *   --> UserDetailsMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code user_detailss}</p>
 * <p><b>Module Deps      :</b> Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Retrieves user by username data from the database.
     *
     * @param email the email input value
     * @return the UserDetails result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .flatMap(role -> Stream.concat(
                        Stream.of(new SimpleGrantedAuthority("ROLE_" + role.getCode())),
                        role.getPermissions().stream()
                                .map(permission -> new SimpleGrantedAuthority(permission.getCode()))
                ))
                .collect(Collectors.toSet());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .disabled(!user.getActive())
                .build();
    }
}