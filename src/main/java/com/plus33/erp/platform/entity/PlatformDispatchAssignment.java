/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformDispatchAssignment.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformDispatchAssignmentController
 * Related Service   : PlatformDispatchAssignmentService, PlatformDispatchAssignmentServiceImpl
 * Related Repository: PlatformDispatchAssignmentRepository
 * Related Entity    : PlatformDispatchAssignment
 * Related DTO       : N/A
 * Related Mapper    : PlatformDispatchAssignmentMapper
 * Related DB Table  : platform_dispatch_assignment
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformDispatchAssignmentRepository, PlatformDispatchAssignmentMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_dispatch_assignment'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformDispatchAssignment}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_dispatch_assignment'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_dispatch_assignment}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_dispatch_assignment")
public class PlatformDispatchAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dispatch_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String dispatchCode;

    @Column(name = "vehicle_id", nullable = false)
    @NotNull
    private Long vehicleId;

    @Column(name = "driver_id", nullable = false)
    @NotNull
    private Long driverId;

    @Column(name = "route_id", nullable = false)
    @NotNull
    private Long routeId;

    @Column(name = "shipment_id", nullable = false)
    @NotNull
    private Long shipmentId;

    @Column(name = "assignment_status", nullable = false)
    @NotNull
    @Size(max = 50)
    private String assignmentStatus; // CREATED, ASSIGNED, ACCEPTED, EN_ROUTE, DELIVERED, FAILED

    @Column(name = "assigned_time", nullable = false)
    @NotNull
    private LocalDateTime assignedTime;

    @Column(name = "accepted_time")
    private LocalDateTime acceptedTime;

    @Column(name = "completed_time")
    private LocalDateTime completedTime;

    @Column(name = "estimated_eta", nullable = false)
    @NotNull
    private LocalDateTime estimatedEta;

    @Column(name = "actual_eta")
    private LocalDateTime actual_eta;

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
     * Retrieves dispatch code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDispatchCode() { return dispatchCode; }
    /**
     * Performs the setDispatchCode operation in this module.
     *
     * @param dispatchCode the dispatchCode input value
     */
    public void setDispatchCode(String dispatchCode) { this.dispatchCode = dispatchCode; }
    /**
     * Retrieves vehicle id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getVehicleId() { return vehicleId; }
    /**
     * Performs the setVehicleId operation in this module.
     *
     * @param vehicleId the vehicleId input value
     */
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    /**
     * Retrieves driver id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDriverId() { return driverId; }
    /**
     * Performs the setDriverId operation in this module.
     *
     * @param driverId the driverId input value
     */
    public void setDriverId(Long driverId) { this.driverId = driverId; }
    /**
     * Retrieves route id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getRouteId() { return routeId; }
    /**
     * Performs the setRouteId operation in this module.
     *
     * @param routeId the routeId input value
     */
    public void setRouteId(Long routeId) { this.routeId = routeId; }
    /**
     * Retrieves shipment id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getShipmentId() { return shipmentId; }
    /**
     * Performs the setShipmentId operation in this module.
     *
     * @param shipmentId the shipmentId input value
     */
    public void setShipmentId(Long shipmentId) { this.shipmentId = shipmentId; }
    /**
     * Retrieves assignment status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAssignmentStatus() { return assignmentStatus; }
    /**
     * Performs the setAssignmentStatus operation in this module.
     *
     * @param assignmentStatus the assignmentStatus input value
     */
    public void setAssignmentStatus(String assignmentStatus) { this.assignmentStatus = assignmentStatus; }
    /**
     * Retrieves assigned time data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getAssignedTime() { return assignedTime; }
    /**
     * Performs the setAssignedTime operation in this module.
     *
     * @param assignedTime the assignedTime input value
     */
    public void setAssignedTime(LocalDateTime assignedTime) { this.assignedTime = assignedTime; }
    /**
     * Retrieves accepted time data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getAcceptedTime() { return acceptedTime; }
    /**
     * Performs the setAcceptedTime operation in this module.
     *
     * @param acceptedTime the acceptedTime input value
     */
    public void setAcceptedTime(LocalDateTime acceptedTime) { this.acceptedTime = acceptedTime; }
    /**
     * Retrieves completed time data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCompletedTime() { return completedTime; }
    /**
     * Performs the setCompletedTime operation in this module.
     *
     * @param completedTime the completedTime input value
     */
    public void setCompletedTime(LocalDateTime completedTime) { this.completedTime = completedTime; }
    /**
     * Retrieves estimated eta data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getEstimatedEta() { return estimatedEta; }
    /**
     * Performs the setEstimatedEta operation in this module.
     *
     * @param estimatedEta the estimatedEta input value
     */
    public void setEstimatedEta(LocalDateTime estimatedEta) { this.estimatedEta = estimatedEta; }
    /**
     * Retrieves actual eta data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getActualEta() { return actual_eta; }
    /**
     * Performs the setActualEta operation in this module.
     *
     * @param actualEta the actualEta input value
     */
    public void setActualEta(LocalDateTime actualEta) { this.actual_eta = actualEta; }
}