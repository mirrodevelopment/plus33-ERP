/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformTwinInstance.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformTwinInstanceController
 * Related Service   : PlatformTwinInstanceService, PlatformTwinInstanceServiceImpl
 * Related Repository: PlatformTwinInstanceRepository
 * Related Entity    : PlatformTwinInstance
 * Related DTO       : N/A
 * Related Mapper    : PlatformTwinInstanceMapper
 * Related DB Table  : platform_twin_instance
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformTwinInstanceRepository, PlatformTwinInstanceMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_twin_instance'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformTwinInstance}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_twin_instance'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_twin_instance}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_twin_instance")
public class PlatformTwinInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "definition_id", nullable = false)
    @NotNull
    private Long definitionId;

    @Column(name = "instance_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String instanceCode;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "ACTIVE";

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
     * Retrieves version data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getVersion() { return version; }
    /**
     * Performs the setVersion operation in this module.
     *
     * @param version the version input value
     */
    public void setVersion(Integer version) { this.version = version; }
    /**
     * Retrieves definition id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDefinitionId() { return definitionId; }
    /**
     * Performs the setDefinitionId operation in this module.
     *
     * @param definitionId the definitionId input value
     */
    public void setDefinitionId(Long definitionId) { this.definitionId = definitionId; }
    /**
     * Retrieves instance code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getInstanceCode() { return instanceCode; }
    /**
     * Performs the setInstanceCode operation in this module.
     *
     * @param instanceCode the instanceCode input value
     */
    public void setInstanceCode(String instanceCode) { this.instanceCode = instanceCode; }
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