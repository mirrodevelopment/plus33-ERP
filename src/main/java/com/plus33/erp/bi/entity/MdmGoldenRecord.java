/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : MdmGoldenRecord.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MdmGoldenRecordController
 * Related Service   : MdmGoldenRecordService, MdmGoldenRecordServiceImpl
 * Related Repository: MdmGoldenRecordRepository
 * Related Entity    : MdmGoldenRecord
 * Related DTO       : N/A
 * Related Mapper    : MdmGoldenRecordMapper
 * Related DB Table  : bi_mdm_golden_record
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MdmGoldenRecordRepository, MdmGoldenRecordMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_mdm_golden_record'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code MdmGoldenRecord}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_mdm_golden_record'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_mdm_golden_record}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_mdm_golden_record")
public class MdmGoldenRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String recordType;

    @Column(name = "display_name", nullable = false)
    @NotNull
    @Size(max = 250)
    private String displayName;

    @Size(max = 250)
    private String email;

    @Size(max = 50)
    private String phone;

    private String address;

    @Column(name = "tax_number")
    @Size(max = 50)
    private String taxNumber;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "ACTIVE";

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

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
     * Retrieves record type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRecordType() { return recordType; }
    /**
     * Performs the setRecordType operation in this module.
     *
     * @param recordType the recordType input value
     */
    public void setRecordType(String recordType) { this.recordType = recordType; }
    /**
     * Retrieves display name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDisplayName() { return displayName; }
    /**
     * Performs the setDisplayName operation in this module.
     *
     * @param displayName the displayName input value
     */
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    /**
     * Retrieves email data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEmail() { return email; }
    /**
     * Performs the setEmail operation in this module.
     *
     * @param email the email input value
     */
    public void setEmail(String email) { this.email = email; }
    /**
     * Retrieves phone data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPhone() { return phone; }
    /**
     * Performs the setPhone operation in this module.
     *
     * @param phone the phone input value
     */
    public void setPhone(String phone) { this.phone = phone; }
    /**
     * Retrieves address data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAddress() { return address; }
    /**
     * Performs the setAddress operation in this module.
     *
     * @param address the address input value
     */
    public void setAddress(String address) { this.address = address; }
    /**
     * Retrieves tax number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTaxNumber() { return taxNumber; }
    /**
     * Performs the setTaxNumber operation in this module.
     *
     * @param taxNumber the taxNumber input value
     */
    public void setTaxNumber(String taxNumber) { this.taxNumber = taxNumber; }
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
    /**
     * Performs the setCreatedAt operation in this module.
     *
     * @param createdAt the createdAt input value
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
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