/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : PaymentTransmissionLog.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentTransmissionLogController
 * Related Service   : PaymentTransmissionLogService, PaymentTransmissionLogServiceImpl
 * Related Repository: PaymentTransmissionLogRepository
 * Related Entity    : PaymentTransmissionLog
 * Related DTO       : N/A
 * Related Mapper    : PaymentTransmissionLogMapper
 * Related DB Table  : payment_transmission_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PaymentTransmissionLogRepository, PaymentTransmissionLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'payment_transmission_logs'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentTransmissionLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'payment_transmission_logs'.</p>
 *
 * <p><b>Database Table   :</b> {@code payment_transmission_logs}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "payment_transmission_logs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTransmissionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", nullable = false)
    private PaymentFile file;

    @Column(name = "transmission_method", nullable = false, length = 30)
    private String transmissionMethod; // API, SFTP

    @Column(nullable = false, length = 30)
    private String status; // SUCCESS, FAILED

    @Column(name = "request_payload", columnDefinition = "TEXT")
    private String requestPayload;

    @Column(name = "response_payload", columnDefinition = "TEXT")
    private String responsePayload;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "transmitted_at", nullable = false, updatable = false)
    private LocalDateTime transmittedAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        transmittedAt = LocalDateTime.now();
    }
}