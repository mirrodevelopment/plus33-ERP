/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : SerialGenealogy.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SerialGenealogyController
 * Related Service   : SerialGenealogyService, SerialGenealogyServiceImpl
 * Related Repository: SerialGenealogyRepository
 * Related Entity    : SerialGenealogy
 * Related DTO       : N/A
 * Related Mapper    : SerialGenealogyMapper
 * Related DB Table  : serial_genealogy
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SerialGenealogyRepository, SerialGenealogyMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'serial_genealogy'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code SerialGenealogy}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'serial_genealogy'.</p>
 *
 * <p><b>Database Table   :</b> {@code serial_genealogy}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "serial_genealogy")
public class SerialGenealogy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "parent_serial_number", length = 100)
    private String parentSerialNumber;

    @Column(name = "child_serial_number", nullable = false, length = 100)
    private String childSerialNumber;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "location_id")
    private Long locationId;

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
     * Retrieves parent serial number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getParentSerialNumber() { return parentSerialNumber; }
    /**
     * Performs the setParentSerialNumber operation in this module.
     *
     * @param parentSerialNumber the parentSerialNumber input value
     */
    public void setParentSerialNumber(String parentSerialNumber) { this.parentSerialNumber = parentSerialNumber; }
    /**
     * Retrieves child serial number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getChildSerialNumber() { return childSerialNumber; }
    /**
     * Performs the setChildSerialNumber operation in this module.
     *
     * @param childSerialNumber the childSerialNumber input value
     */
    public void setChildSerialNumber(String childSerialNumber) { this.childSerialNumber = childSerialNumber; }
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
     * Retrieves event type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEventType() { return eventType; }
    /**
     * Performs the setEventType operation in this module.
     *
     * @param eventType the eventType input value
     */
    public void setEventType(String eventType) { this.eventType = eventType; }
    /**
     * Retrieves location id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getLocationId() { return locationId; }
    /**
     * Performs the setLocationId operation in this module.
     *
     * @param locationId the locationId input value
     */
    public void setLocationId(Long locationId) { this.locationId = locationId; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}