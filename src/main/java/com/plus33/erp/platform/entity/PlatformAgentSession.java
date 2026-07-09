/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformAgentSession.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformAgentSessionController
 * Related Service   : PlatformAgentSessionService, PlatformAgentSessionServiceImpl
 * Related Repository: PlatformAgentSessionRepository
 * Related Entity    : PlatformAgentSession
 * Related DTO       : N/A
 * Related Mapper    : PlatformAgentSessionMapper
 * Related DB Table  : platform_agent_session
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformAgentSessionRepository, PlatformAgentSessionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_agent_session'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformAgentSession}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_agent_session'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_agent_session}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_agent_session")
public class PlatformAgentSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "session_token", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String sessionToken;

    @Column(name = "user_identity", nullable = false)
    @NotNull
    @Size(max = 100)
    private String userIdentity;

    @Column(nullable = false)
    @NotNull
    private Boolean active = true;

    @Column(name = "created_at", nullable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Retrieves id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getId() { return id; }
    /**
     * Performs the setId operation in this module.
     *
     * @param id the unique database ID of the resource
     */
    public void setId(Long id) { this.id = id; }
    /**
     * Retrieves version data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getVersion() { return version; }
    /**
     * Performs the setVersion operation in this module.
     *
     * @param version the version input value
     */
    public void setVersion(Integer version) { this.version = version; }
    /**
     * Retrieves session token data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSessionToken() { return sessionToken; }
    /**
     * Performs the setSessionToken operation in this module.
     *
     * @param sessionToken the sessionToken input value
     */
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }
    /**
     * Retrieves user identity data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getUserIdentity() { return userIdentity; }
    /**
     * Performs the setUserIdentity operation in this module.
     *
     * @param userIdentity the userIdentity input value
     */
    public void setUserIdentity(String userIdentity) { this.userIdentity = userIdentity; }
    /**
     * Retrieves active data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getActive() { return active; }
    /**
     * Performs the setActive operation in this module.
     *
     * @param active the active input value
     */
    public void setActive(Boolean active) { this.active = active; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Performs the setCreatedAt operation in this module.
     *
     * @param createdAt the createdAt input value
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}