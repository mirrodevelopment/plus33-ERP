/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.entity
 * File              : InstalledAsset.java
 * Purpose           : JPA Entity representing a persistent database record in Esm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InstalledAssetController
 * Related Service   : InstalledAssetService, InstalledAssetServiceImpl
 * Related Repository: InstalledAssetRepository
 * Related Entity    : InstalledAsset
 * Related DTO       : N/A
 * Related Mapper    : InstalledAssetMapper
 * Related DB Table  : esm_installed_assets
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InstalledAssetRepository, InstalledAssetMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'esm_installed_assets'. Defines persistent domain object for Esm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.esm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code InstalledAsset}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'esm_installed_assets'.</p>
 *
 * <p><b>Database Table   :</b> {@code esm_installed_assets}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "esm_installed_assets")
public class InstalledAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "serial_number", nullable = false, unique = true, length = 100)
    private String serialNumber;

    @Column(name = "location_description", length = 255)
    private String locationDescription;

    @Column(nullable = false, length = 30)
    private String status = "ACTIVE";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and setters
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
     * Retrieves company id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; }
    /**
     * Performs the setCompanyId operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     */
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    /**
     * Retrieves customer id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCustomerId() { return customerId; }
    /**
     * Performs the setCustomerId operation in this module.
     *
     * @param customerId the customerId input value
     */
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    /**
     * Retrieves product id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getProductId() { return productId; }
    /**
     * Performs the setProductId operation in this module.
     *
     * @param productId the productId input value
     */
    public void setProductId(Long productId) { this.productId = productId; }
    /**
     * Retrieves serial number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSerialNumber() { return serialNumber; }
    /**
     * Performs the setSerialNumber operation in this module.
     *
     * @param serialNumber the serialNumber input value
     */
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    /**
     * Retrieves location description data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getLocationDescription() { return locationDescription; }
    /**
     * Performs the setLocationDescription operation in this module.
     *
     * @param locationDescription the locationDescription input value
     */
    public void setLocationDescription(String locationDescription) { this.locationDescription = locationDescription; }
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
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}