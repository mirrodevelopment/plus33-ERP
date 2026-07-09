/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.entity
 * File              : IntegrationLock.java
 * Purpose           : JPA Entity representing a persistent database record in Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IntegrationLockController
 * Related Service   : IntegrationLockService, IntegrationLockServiceImpl
 * Related Repository: IntegrationLockRepository
 * Related Entity    : IntegrationLock
 * Related DTO       : N/A
 * Related Mapper    : IntegrationLockMapper
 * Related DB Table  : integration_lock
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IntegrationLockRepository, IntegrationLockMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'integration_lock'. Defines persistent domain object for Integration Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code IntegrationLock}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'integration_lock'.</p>
 *
 * <p><b>Database Table   :</b> {@code integration_lock}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "integration_lock")
public class IntegrationLock {
    @Id
    @Column(name = "lock_key")
    @NotNull
    @Size(max = 250)
    private String lockKey;

    @Column(name = "owner_id", nullable = false)
    @NotNull
    @Size(max = 250)
    private String ownerId;

    @Column(name = "expires_at", nullable = false)
    @NotNull
    private LocalDateTime expiresAt;

    /**
     * Retrieves lock key data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getLockKey() { return lockKey; }
    /**
     * Performs the setLockKey operation in this module.
     *
     * @param lockKey the lockKey input value
     */
    public void setLockKey(String lockKey) { this.lockKey = lockKey; }
    /**
     * Retrieves owner id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOwnerId() { return ownerId; }
    /**
     * Performs the setOwnerId operation in this module.
     *
     * @param ownerId the ownerId input value
     */
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    /**
     * Retrieves expires at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getExpiresAt() { return expiresAt; }
    /**
     * Performs the setExpiresAt operation in this module.
     *
     * @param expiresAt the expiresAt input value
     */
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
}