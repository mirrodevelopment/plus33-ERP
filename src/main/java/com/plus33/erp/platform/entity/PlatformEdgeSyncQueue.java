/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformEdgeSyncQueue.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformEdgeSyncQueueController
 * Related Service   : PlatformEdgeSyncQueueService, PlatformEdgeSyncQueueServiceImpl
 * Related Repository: PlatformEdgeSyncQueueRepository
 * Related Entity    : PlatformEdgeSyncQueue
 * Related DTO       : N/A
 * Related Mapper    : PlatformEdgeSyncQueueMapper
 * Related DB Table  : platform_edge_sync_queue
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformEdgeSyncQueueRepository, PlatformEdgeSyncQueueMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_edge_sync_queue'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformEdgeSyncQueue}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_edge_sync_queue'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_edge_sync_queue}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_edge_sync_queue")
public class PlatformEdgeSyncQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "node_id", nullable = false)
    @NotNull
    private Long nodeId;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String payload;

    @Column(name = "payload_hash", nullable = false)
    @NotNull
    @Size(max = 64)
    private String payloadHash;

    @Column(name = "payload_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String payloadType;

    @Column(nullable = false)
    @NotNull
    private Integer priority = 0;

    @Column(name = "sequence_number", nullable = false)
    @NotNull
    private Long sequenceNumber;

    @Column(name = "retry_count", nullable = false)
    @NotNull
    private Integer retryCount = 0;

    @Column(name = "max_retry", nullable = false)
    @NotNull
    private Integer maxRetry = 5;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status; // PENDING, SENT, ACKNOWLEDGED, FAILED

    @Column(nullable = false)
    @NotNull
    private Boolean compressed = false;

    @Column(nullable = false)
    @NotNull
    private Boolean encrypted = false;

    @Size(max = 64)
    private String checksum;

    @Column(name = "created_at", nullable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "acknowledged_at")
    private LocalDateTime acknowledgedAt;

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
     * Retrieves node id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getNodeId() { return nodeId; }
    /**
     * Performs the setNodeId operation in this module.
     *
     * @param nodeId the nodeId input value
     */
    public void setNodeId(Long nodeId) { this.nodeId = nodeId; }
    /**
     * Retrieves payload data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPayload() { return payload; }
    /**
     * Performs the setPayload operation in this module.
     *
     * @param payload the payload input value
     */
    public void setPayload(String payload) { this.payload = payload; }
    /**
     * Retrieves payload hash data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPayloadHash() { return payloadHash; }
    /**
     * Performs the setPayloadHash operation in this module.
     *
     * @param payloadHash the payloadHash input value
     */
    public void setPayloadHash(String payloadHash) { this.payloadHash = payloadHash; }
    /**
     * Retrieves payload type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPayloadType() { return payloadType; }
    /**
     * Performs the setPayloadType operation in this module.
     *
     * @param payloadType the payloadType input value
     */
    public void setPayloadType(String payloadType) { this.payloadType = payloadType; }
    /**
     * Retrieves priority data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getPriority() { return priority; }
    /**
     * Performs the setPriority operation in this module.
     *
     * @param priority the priority input value
     */
    public void setPriority(Integer priority) { this.priority = priority; }
    /**
     * Retrieves sequence number data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSequenceNumber() { return sequenceNumber; }
    /**
     * Performs the setSequenceNumber operation in this module.
     *
     * @param sequenceNumber the sequenceNumber input value
     */
    public void setSequenceNumber(Long sequenceNumber) { this.sequenceNumber = sequenceNumber; }
    /**
     * Retrieves retry count data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getRetryCount() { return retryCount; }
    /**
     * Performs the setRetryCount operation in this module.
     *
     * @param retryCount the retryCount input value
     */
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }
    /**
     * Retrieves max retry data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getMaxRetry() { return maxRetry; }
    /**
     * Performs the setMaxRetry operation in this module.
     *
     * @param maxRetry the maxRetry input value
     */
    public void setMaxRetry(Integer maxRetry) { this.maxRetry = maxRetry; }
    /**
     * Retrieves status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * Retrieves compressed data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getCompressed() { return compressed; }
    /**
     * Performs the setCompressed operation in this module.
     *
     * @param compressed the compressed input value
     */
    public void setCompressed(Boolean compressed) { this.compressed = compressed; }
    /**
     * Retrieves encrypted data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getEncrypted() { return encrypted; }
    /**
     * Performs the setEncrypted operation in this module.
     *
     * @param encrypted the encrypted input value
     */
    public void setEncrypted(Boolean encrypted) { this.encrypted = encrypted; }
    /**
     * Retrieves checksum data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getChecksum() { return checksum; }
    /**
     * Performs the setChecksum operation in this module.
     *
     * @param checksum the checksum input value
     */
    public void setChecksum(String checksum) { this.checksum = checksum; }
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
    /**
     * Retrieves sent at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getSentAt() { return sentAt; }
    /**
     * Performs the setSentAt operation in this module.
     *
     * @param sentAt the sentAt input value
     */
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
    /**
     * Retrieves acknowledged at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getAcknowledgedAt() { return acknowledgedAt; }
    /**
     * Performs the setAcknowledgedAt operation in this module.
     *
     * @param acknowledgedAt the acknowledgedAt input value
     */
    public void setAcknowledgedAt(LocalDateTime acknowledgedAt) { this.acknowledgedAt = acknowledgedAt; }
}