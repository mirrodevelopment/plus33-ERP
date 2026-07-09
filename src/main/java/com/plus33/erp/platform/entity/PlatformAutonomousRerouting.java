/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformAutonomousRerouting.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformAutonomousReroutingController
 * Related Service   : PlatformAutonomousReroutingService, PlatformAutonomousReroutingServiceImpl
 * Related Repository: PlatformAutonomousReroutingRepository
 * Related Entity    : PlatformAutonomousRerouting
 * Related DTO       : N/A
 * Related Mapper    : PlatformAutonomousReroutingMapper
 * Related DB Table  : platform_autonomous_rerouting
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformAutonomousReroutingRepository, PlatformAutonomousReroutingMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_autonomous_rerouting'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformAutonomousRerouting}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_autonomous_rerouting'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_autonomous_rerouting}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_autonomous_rerouting")
public class PlatformAutonomousRerouting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transit_route_id", nullable = false)
    @NotNull
    private Long transitRouteId;

    @Column(name = "policy_id", nullable = false)
    @NotNull
    private Long policyId;

    @Column(nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal confidence;

    @Column(name = "suggested_route_json", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String suggestedRouteJson;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "PENDING";

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
     * Retrieves transit route id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getTransitRouteId() { return transitRouteId; }
    /**
     * Performs the setTransitRouteId operation in this module.
     *
     * @param transitRouteId the transitRouteId input value
     */
    public void setTransitRouteId(Long transitRouteId) { this.transitRouteId = transitRouteId; }
    /**
     * Retrieves policy id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPolicyId() { return policyId; }
    /**
     * Performs the setPolicyId operation in this module.
     *
     * @param policyId the policyId input value
     */
    public void setPolicyId(Long policyId) { this.policyId = policyId; }
    /**
     * Retrieves confidence data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getConfidence() { return confidence; }
    /**
     * Performs the setConfidence operation in this module.
     *
     * @param confidence the confidence input value
     */
    public void setConfidence(BigDecimal confidence) { this.confidence = confidence; }
    /**
     * Retrieves suggested route json data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSuggestedRouteJson() { return suggestedRouteJson; }
    /**
     * Performs the setSuggestedRouteJson operation in this module.
     *
     * @param suggestedRouteJson the suggestedRouteJson input value
     */
    public void setSuggestedRouteJson(String suggestedRouteJson) { this.suggestedRouteJson = suggestedRouteJson; }
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