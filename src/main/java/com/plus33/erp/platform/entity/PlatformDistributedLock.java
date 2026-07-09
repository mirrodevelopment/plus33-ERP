/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformDistributedLock.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformDistributedLockController
 * Related Service   : PlatformDistributedLockService, PlatformDistributedLockServiceImpl
 * Related Repository: PlatformDistributedLockRepository
 * Related Entity    : PlatformDistributedLock
 * Related DTO       : N/A
 * Related Mapper    : PlatformDistributedLockMapper
 * Related DB Table  : platform_distributed_lock
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformDistributedLockRepository, PlatformDistributedLockMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_distributed_lock'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformDistributedLock}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_distributed_lock'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_distributed_lock}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_distributed_lock")
public class PlatformDistributedLock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lock_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 150)
    private String lockName;

    @Column(name = "owner_node", nullable = false)
    @NotNull
    @Size(max = 100)
    private String ownerNode;

    @Column(name = "expires_at", nullable = false)
    @NotNull
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime heartbeat = LocalDateTime.now();

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
     * Retrieves lock name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getLockName() { return lockName; }
    /**
     * Performs the setLockName operation in this module.
     *
     * @param lockName the lockName input value
     */
    public void setLockName(String lockName) { this.lockName = lockName; }
    /**
     * Retrieves owner node data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOwnerNode() { return ownerNode; }
    /**
     * Performs the setOwnerNode operation in this module.
     *
     * @param ownerNode the ownerNode input value
     */
    public void setOwnerNode(String ownerNode) { this.ownerNode = ownerNode; }
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
    /**
     * Retrieves heartbeat data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getHeartbeat() { return heartbeat; }
    /**
     * Performs the setHeartbeat operation in this module.
     *
     * @param heartbeat the heartbeat input value
     */
    public void setHeartbeat(LocalDateTime heartbeat) { this.heartbeat = heartbeat; }
}