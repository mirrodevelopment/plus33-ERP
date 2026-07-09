/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.entity
 * File              : HcmCandidate.java
 * Purpose           : JPA Entity representing a persistent database record in Hcm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: HcmCandidateController
 * Related Service   : HcmCandidateService, HcmCandidateServiceImpl
 * Related Repository: HcmCandidateRepository
 * Related Entity    : HcmCandidate
 * Related DTO       : N/A
 * Related Mapper    : HcmCandidateMapper
 * Related DB Table  : hcm_candidates
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : HcmCandidateRepository, HcmCandidateMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'hcm_candidates'. Defines persistent domain object for Hcm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.hcm.entity;

import jakarta.persistence.*;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code HcmCandidate}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'hcm_candidates'.</p>
 *
 * <p><b>Database Table   :</b> {@code hcm_candidates}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "hcm_candidates")
public class HcmCandidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "requisition_id", nullable = false)
    private Long requisitionId;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 30)
    private String status = "APPLIED";

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
     * Retrieves requisition id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getRequisitionId() { return requisitionId; }
    /**
     * Performs the setRequisitionId operation in this module.
     *
     * @param requisitionId the requisitionId input value
     */
    public void setRequisitionId(Long requisitionId) { this.requisitionId = requisitionId; }
    /**
     * Retrieves first name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFirstName() { return firstName; }
    /**
     * Performs the setFirstName operation in this module.
     *
     * @param firstName the firstName input value
     */
    public void setFirstName(String firstName) { this.firstName = firstName; }
    /**
     * Retrieves last name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getLastName() { return lastName; }
    /**
     * Performs the setLastName operation in this module.
     *
     * @param lastName the lastName input value
     */
    public void setLastName(String lastName) { this.lastName = lastName; }
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
}