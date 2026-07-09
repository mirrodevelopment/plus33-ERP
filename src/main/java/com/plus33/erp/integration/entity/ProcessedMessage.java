/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.entity
 * File              : ProcessedMessage.java
 * Purpose           : JPA Entity representing a persistent database record in Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProcessedMessageController
 * Related Service   : ProcessedMessageService, ProcessedMessageServiceImpl
 * Related Repository: ProcessedMessageRepository
 * Related Entity    : ProcessedMessage
 * Related DTO       : N/A
 * Related Mapper    : ProcessedMessageMapper
 * Related DB Table  : processed_messages
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProcessedMessageRepository, ProcessedMessageMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'processed_messages'. Defines persistent domain object for Integration Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code ProcessedMessage}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'processed_messages'.</p>
 *
 * <p><b>Database Table   :</b> {@code processed_messages}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "processed_messages")
public class ProcessedMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message_id", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String messageId;

    @Column(name = "consumer_group", nullable = false)
    @NotNull
    @Size(max = 100)
    private String consumerGroup;

    @Column(name = "processed_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime processedAt = LocalDateTime.now();

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
     * Retrieves message id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMessageId() { return messageId; }
    /**
     * Performs the setMessageId operation in this module.
     *
     * @param messageId the messageId input value
     */
    public void setMessageId(String messageId) { this.messageId = messageId; }
    /**
     * Retrieves consumer group data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getConsumerGroup() { return consumerGroup; }
    /**
     * Performs the setConsumerGroup operation in this module.
     *
     * @param consumerGroup the consumerGroup input value
     */
    public void setConsumerGroup(String consumerGroup) { this.consumerGroup = consumerGroup; }
    /**
     * Retrieves processed at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getProcessedAt() { return processedAt; }
    /**
     * Performs the setProcessedAt operation in this module.
     *
     * @param processedAt the processedAt input value
     */
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
}