/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformAgentMemory.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformAgentMemoryController
 * Related Service   : PlatformAgentMemoryService, PlatformAgentMemoryServiceImpl
 * Related Repository: PlatformAgentMemoryRepository
 * Related Entity    : PlatformAgentMemory
 * Related DTO       : N/A
 * Related Mapper    : PlatformAgentMemoryMapper
 * Related DB Table  : platform_agent_memory
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformAgentMemoryRepository, PlatformAgentMemoryMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_agent_memory'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformAgentMemory}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_agent_memory'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_agent_memory}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_agent_memory")
public class PlatformAgentMemory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "session_id", nullable = false)
    @NotNull
    private Long sessionId;

    @Column(name = "memory_scope", nullable = false)
    @NotNull
    @Size(max = 50)
    private String memoryScope;

    @Column(name = "memory_key", nullable = false)
    @NotNull
    @Size(max = 150)
    private String memoryKey;

    @Column(name = "memory_value", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String memoryValue;

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

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
     * Retrieves session id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSessionId() { return sessionId; }
    /**
     * Performs the setSessionId operation in this module.
     *
     * @param sessionId active session identifier
     */
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }
    /**
     * Retrieves memory scope data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMemoryScope() { return memoryScope; }
    /**
     * Performs the setMemoryScope operation in this module.
     *
     * @param memoryScope the memoryScope input value
     */
    public void setMemoryScope(String memoryScope) { this.memoryScope = memoryScope; }
    /**
     * Retrieves memory key data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMemoryKey() { return memoryKey; }
    /**
     * Performs the setMemoryKey operation in this module.
     *
     * @param memoryKey the memoryKey input value
     */
    public void setMemoryKey(String memoryKey) { this.memoryKey = memoryKey; }
    /**
     * Retrieves memory value data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMemoryValue() { return memoryValue; }
    /**
     * Performs the setMemoryValue operation in this module.
     *
     * @param memoryValue the memoryValue input value
     */
    public void setMemoryValue(String memoryValue) { this.memoryValue = memoryValue; }
    /**
     * Retrieves updated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    /**
     * Performs the setUpdatedAt operation in this module.
     *
     * @param updatedAt the updatedAt input value
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}