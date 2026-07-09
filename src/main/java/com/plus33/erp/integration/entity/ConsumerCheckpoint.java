/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.entity
 * File              : ConsumerCheckpoint.java
 * Purpose           : JPA Entity representing a persistent database record in Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ConsumerCheckpointController
 * Related Service   : ConsumerCheckpointService, ConsumerCheckpointServiceImpl
 * Related Repository: ConsumerCheckpointRepository
 * Related Entity    : ConsumerCheckpoint
 * Related DTO       : N/A
 * Related Mapper    : ConsumerCheckpointMapper
 * Related DB Table  : consumer_checkpoint
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ConsumerCheckpointRepository, ConsumerCheckpointMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'consumer_checkpoint'. Defines persistent domain object for Integration Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code ConsumerCheckpoint}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'consumer_checkpoint'.</p>
 *
 * <p><b>Database Table   :</b> {@code consumer_checkpoint}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "consumer_checkpoint", uniqueConstraints = @UniqueConstraint(columnNames = {"group_name", "topic"}))
public class ConsumerCheckpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String groupName;

    @Column(name = "consumer_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String consumerName;

    @Column(name = "partition_assignment")
    @Size(max = 250)
    private String partitionAssignment;

    @Column(name = "rebalance_generation", nullable = false)
    @NotNull
    private Integer rebalanceGeneration = 0;

    @Column(nullable = false)
    @NotNull
    @Size(max = 250)
    private String topic;

    @Column(name = "checkpoint_offset", nullable = false)
    @NotNull
    private Long checkpointOffset = 0L;

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
     * Retrieves group name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getGroupName() { return groupName; }
    /**
     * Performs the setGroupName operation in this module.
     *
     * @param groupName the groupName input value
     */
    public void setGroupName(String groupName) { this.groupName = groupName; }
    /**
     * Retrieves consumer name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getConsumerName() { return consumerName; }
    /**
     * Performs the setConsumerName operation in this module.
     *
     * @param consumerName the consumerName input value
     */
    public void setConsumerName(String consumerName) { this.consumerName = consumerName; }
    /**
     * Retrieves partition assignment data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPartitionAssignment() { return partitionAssignment; }
    /**
     * Performs the setPartitionAssignment operation in this module.
     *
     * @param partitionAssignment the partitionAssignment input value
     */
    public void setPartitionAssignment(String partitionAssignment) { this.partitionAssignment = partitionAssignment; }
    /**
     * Retrieves rebalance generation data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getRebalanceGeneration() { return rebalanceGeneration; }
    /**
     * Performs the setRebalanceGeneration operation in this module.
     *
     * @param rebalanceGeneration the rebalanceGeneration input value
     */
    public void setRebalanceGeneration(Integer rebalanceGeneration) { this.rebalanceGeneration = rebalanceGeneration; }
    /**
     * Retrieves topic data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTopic() { return topic; }
    /**
     * Performs the setTopic operation in this module.
     *
     * @param topic the topic input value
     */
    public void setTopic(String topic) { this.topic = topic; }
    /**
     * Retrieves checkpoint offset data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCheckpointOffset() { return checkpointOffset; }
    /**
     * Performs the setCheckpointOffset operation in this module.
     *
     * @param checkpointOffset the checkpointOffset input value
     */
    public void setCheckpointOffset(Long checkpointOffset) { this.checkpointOffset = checkpointOffset; }
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