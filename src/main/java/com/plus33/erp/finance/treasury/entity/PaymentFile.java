/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : PaymentFile.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentFileController
 * Related Service   : PaymentFileService, PaymentFileServiceImpl
 * Related Repository: PaymentFileRepository
 * Related Entity    : PaymentFile
 * Related DTO       : N/A
 * Related Mapper    : PaymentFileMapper
 * Related DB Table  : payment_files
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PaymentFileRepository, PaymentFileMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'payment_files'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentFile}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'payment_files'.</p>
 *
 * <p><b>Database Table   :</b> {@code payment_files}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "payment_files")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false, unique = true)
    private PaymentBatch batch;

    @Column(name = "file_name", nullable = false, length = 150)
    private String fileName;

    @Column(name = "file_format", nullable = false, length = 30)
    private String fileFormat; // ISO20022_XML, SEPA_XML, NACHA_TXT, SWIFT_MT101

    @Column(name = "file_content", nullable = false, columnDefinition = "TEXT")
    private String fileContent;

    @Column(name = "generated_at", nullable = false, updatable = false)
    private LocalDateTime generatedAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
    }
}