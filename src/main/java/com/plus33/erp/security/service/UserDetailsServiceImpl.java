/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Security Module
 * Package           : com.plus33.erp.security.service
 * File              : UserDetailsServiceImpl.java
 * Purpose           : Business logic service layer for Security Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: UserDetailsController
 * Related Service   : UserDetailsServiceImpl
 * Related Repository: UserRepository
 * Related Entity    : UserDetails
 * Related DTO       : N/A
 * Related Mapper    : UserDetailsMapper
 * Related DB Table  : user_detailss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : UserDetailsController, UserDetailsServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Security Module. Implements UserDetailsService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
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