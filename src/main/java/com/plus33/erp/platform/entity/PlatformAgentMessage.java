/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformAgentMessage.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformAgentMessageController
 * Related Service   : PlatformAgentMessageService, PlatformAgentMessageServiceImpl
 * Related Repository: PlatformAgentMessageRepository
 * Related Entity    : PlatformAgentMessage
 * Related DTO       : N/A
 * Related Mapper    : PlatformAgentMessageMapper
 * Related DB Table  : platform_agent_message
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformAgentMessageRepository, PlatformAgentMessageMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_agent_message'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformAgentMessage}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_agent_message'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_agent_message}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_agent_message")
public class PlatformAgentMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false)
    @NotNull
    private Long sessionId;

    @Column(name = "sender_role", nullable = false)
    @NotNull
    @Size(max = 50)
    private String senderRole;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String content;

    @Column(name = "sent_at", nullable = false)
    @NotNull
    private LocalDateTime sentAt = LocalDateTime.now();

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
     * Retrieves sender role data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSenderRole() { return senderRole; }
    /**
     * Performs the setSenderRole operation in this module.
     *
     * @param senderRole the senderRole input value
     */
    public void setSenderRole(String senderRole) { this.senderRole = senderRole; }
    /**
     * Retrieves content data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getContent() { return content; }
    /**
     * Performs the setContent operation in this module.
     *
     * @param content the content input value
     */
    public void setContent(String content) { this.content = content; }
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
}