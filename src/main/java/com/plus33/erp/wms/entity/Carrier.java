/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : Carrier.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CarrierController
 * Related Service   : CarrierService, CarrierServiceImpl
 * Related Repository: CarrierRepository
 * Related Entity    : Carrier
 * Related DTO       : N/A
 * Related Mapper    : CarrierMapper
 * Related DB Table  : carriers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CarrierRepository, CarrierMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'carriers'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "carriers",
       uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "code"}))
/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code Carrier}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'carriers'.</p>
 *
 * <p><b>Database Table   :</b> {@code carriers}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public class Carrier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(nullable = false, length = 30)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    /** COURIER, LTL, FTL, AIR, OCEAN, RAIL, PARCEL, LOCAL */
    @Column(name = "carrier_type", nullable = false, length = 30)
    private String carrierType = "COURIER";

    /** Provider implementation key — e.g. FEDEX, DHL, UPS, LOCAL, CUSTOM */
    @Column(name = "provider_key", length = 50)
    private String providerKey;

    @Column(name = "api_endpoint", length = 500)
    private String apiEndpoint;

    /** Vault reference or secret name — never the actual API key */
    @Column(name = "api_key_ref", length = 100)
    private String apiKeyRef;

    @Column(name = "account_number", length = 100)
    private String accountNumber;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

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
     * Retrieves code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCode() { return code; }
    /**
     * Performs the setCode operation in this module.
     *
     * @param code the code input value
     */
    public void setCode(String code) { this.code = code; }
    /**
     * Retrieves name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getName() { return name; }
    /**
     * Performs the setName operation in this module.
     *
     * @param name the name input value
     */
    public void setName(String name) { this.name = name; }
    /**
     * Retrieves carrier type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCarrierType() { return carrierType; }
    /**
     * Performs the setCarrierType operation in this module.
     *
     * @param carrierType the carrierType input value
     */
    public void setCarrierType(String carrierType) { this.carrierType = carrierType; }
    /**
     * Retrieves provider key data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProviderKey() { return providerKey; }
    /**
     * Performs the setProviderKey operation in this module.
     *
     * @param providerKey the providerKey input value
     */
    public void setProviderKey(String providerKey) { this.providerKey = providerKey; }
    /**
     * Retrieves api endpoint data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getApiEndpoint() { return apiEndpoint; }
    /**
     * Performs the setApiEndpoint operation in this module.
     *
     * @param apiEndpoint the apiEndpoint input value
     */
    public void setApiEndpoint(String apiEndpoint) { this.apiEndpoint = apiEndpoint; }
    /**
     * Retrieves api key ref data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getApiKeyRef() { return apiKeyRef; }
    /**
     * Performs the setApiKeyRef operation in this module.
     *
     * @param apiKeyRef the apiKeyRef input value
     */
    public void setApiKeyRef(String apiKeyRef) { this.apiKeyRef = apiKeyRef; }
    /**
     * Retrieves account number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAccountNumber() { return accountNumber; }
    /**
     * Performs the setAccountNumber operation in this module.
     *
     * @param accountNumber the accountNumber input value
     */
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    /**
     * Performs the isActive operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean isActive() { return active; }
    /**
     * Performs the setActive operation in this module.
     *
     * @param active the active input value
     */
    public void setActive(boolean active) { this.active = active; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
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