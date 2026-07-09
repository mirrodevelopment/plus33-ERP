/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.entity
 * File              : FixedAssetAttachmentVersion.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetAttachmentVersionController
 * Related Service   : FixedAssetAttachmentVersionService, FixedAssetAttachmentVersionServiceImpl
 * Related Repository: FixedAssetAttachmentVersionRepository
 * Related Entity    : FixedAssetAttachmentVersion
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetAttachmentVersionMapper
 * Related DB Table  : fixed_asset_attachment_versions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FixedAssetAttachmentVersionRepository, FixedAssetAttachmentVersionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'fixed_asset_attachment_versions'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.assets.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "fixed_asset_attachment_versions", uniqueConstraints = {
    @UniqueConstraint(name = "uk_attachment_version", columnNames = {"attachment_id", "version_number"})
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetAttachmentVersion}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'fixed_asset_attachment_versions'.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_asset_attachment_versions}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedAssetAttachmentVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachment_id", nullable = false)
    private FixedAssetAttachment attachment;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @Column(name = "file_url", nullable = false, length = 255)
    private String fileUrl;

    @Column(name = "uploaded_by", nullable = false, length = 100)
    private String uploadedBy;

    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }
}