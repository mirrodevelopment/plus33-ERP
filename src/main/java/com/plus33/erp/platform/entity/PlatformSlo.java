/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformSlo.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformSloController
 * Related Service   : PlatformSloService, PlatformSloServiceImpl
 * Related Repository: PlatformSloRepository
 * Related Entity    : PlatformSlo
 * Related DTO       : N/A
 * Related Mapper    : PlatformSloMapper
 * Related DB Table  : platform_slo
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformSloRepository, PlatformSloMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_slo'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformSlo}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_slo'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_slo}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_slo")
public class PlatformSlo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(nullable = false, unique = true)
    @NotNull
    @Size(max = 150)
    private String name;

    @Column(name = "target_percentage", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal targetPercentage;

    @Column(name = "service_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String serviceName;

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
     * Retrieves target percentage data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTargetPercentage() { return targetPercentage; }
    /**
     * Performs the setTargetPercentage operation in this module.
     *
     * @param targetPercentage the targetPercentage input value
     */
    public void setTargetPercentage(BigDecimal targetPercentage) { this.targetPercentage = targetPercentage; }
    /**
     * Retrieves service name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getServiceName() { return serviceName; }
    /**
     * Performs the setServiceName operation in this module.
     *
     * @param serviceName the serviceName input value
     */
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
}