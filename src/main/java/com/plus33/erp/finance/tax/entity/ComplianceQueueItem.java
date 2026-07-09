/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.entity
 * File              : ComplianceQueueItem.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ComplianceQueueItemController
 * Related Service   : ComplianceQueueItemService, ComplianceQueueItemServiceImpl
 * Related Repository: ComplianceQueueItemRepository
 * Related Entity    : ComplianceQueueItem
 * Related DTO       : N/A
 * Related Mapper    : ComplianceQueueItemMapper
 * Related DB Table  : compliance_queue_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ComplianceQueueItemRepository, ComplianceQueueItemMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'compliance_queue_items'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.tax.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code ComplianceQueueItem}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'compliance_queue_items'.</p>
 *
 * <p><b>Database Table   :</b> {@code compliance_queue_items}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "compliance_queue_items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplianceQueueItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "document_type", nullable = false, length = 50)
    private String documentType;

    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Column(name = "provider_type", nullable = false, length = 50)
    private String providerType;

    @Column(name = "payload", nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Builder.Default
    @Column(name = "status", nullable = false, length = 30)
    private String status = "PENDING"; // PENDING, PROCESSING, COMPLETED, FAILED, DEAD_LETTER

    @Builder.Default
    @Column(name = "retry_count", nullable = false)
    private int retryCount = 0;

    @Builder.Default
    @Column(name = "max_retries", nullable = false)
    private int maxRetries = 3;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}