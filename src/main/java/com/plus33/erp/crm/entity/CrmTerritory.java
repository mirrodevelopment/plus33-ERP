/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.entity
 * File              : CrmTerritory.java
 * Purpose           : JPA Entity representing a persistent database record in Crm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CrmTerritoryController
 * Related Service   : CrmTerritoryService, CrmTerritoryServiceImpl
 * Related Repository: CrmTerritoryRepository
 * Related Entity    : CrmTerritory
 * Related DTO       : N/A
 * Related Mapper    : CrmTerritoryMapper
 * Related DB Table  : crm_territories
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CrmTerritoryRepository, CrmTerritoryMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'crm_territories'. Defines persistent domain object for Crm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.crm.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code CrmTerritory}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'crm_territories'.</p>
 *
 * <p><b>Database Table   :</b> {@code crm_territories}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "crm_territories")
public class CrmTerritory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "region_name", nullable = false, length = 100)
    private String regionName;

    @Column(name = "postal_code_range", length = 50)
    private String postalCodeRange;

    @Column(name = "sales_rep_id")
    private Long salesRepId;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "version_number", nullable = false)
    private int versionNumber = 0;

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
     * Retrieves region name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRegionName() { return regionName; }
    /**
     * Performs the setRegionName operation in this module.
     *
     * @param regionName the regionName input value
     */
    public void setRegionName(String regionName) { this.regionName = regionName; }
    /**
     * Retrieves postal code range data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPostalCodeRange() { return postalCodeRange; }
    /**
     * Performs the setPostalCodeRange operation in this module.
     *
     * @param range the range input value
     */
    public void setPostalCodeRange(String range) { this.postalCodeRange = range; }
    /**
     * Retrieves sales rep id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSalesRepId() { return salesRepId; }
    /**
     * Performs the setSalesRepId operation in this module.
     *
     * @param repId the repId input value
     */
    public void setSalesRepId(Long repId) { this.salesRepId = repId; }
    /**
     * Retrieves effective from data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    /**
     * Performs the setEffectiveFrom operation in this module.
     *
     * @param from the from input value
     */
    public void setEffectiveFrom(LocalDate from) { this.effectiveFrom = from; }
    /**
     * Retrieves effective to data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveTo() { return effectiveTo; }
    /**
     * Performs the setEffectiveTo operation in this module.
     *
     * @param to the to input value
     */
    public void setEffectiveTo(LocalDate to) { this.effectiveTo = to; }
    /**
     * Retrieves version number data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getVersionNumber() { return versionNumber; }
    /**
     * Performs the setVersionNumber operation in this module.
     *
     * @param version the version input value
     */
    public void setVersionNumber(int version) { this.versionNumber = version; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}