/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : ProductionOrderOperation.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionOrderOperationController
 * Related Service   : ProductionOrderOperationService, ProductionOrderOperationServiceImpl
 * Related Repository: ProductionOrderOperationRepository
 * Related Entity    : ProductionOrderOperation
 * Related DTO       : N/A
 * Related Mapper    : ProductionOrderOperationMapper
 * Related DB Table  : production_order_operations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProductionOrderOperationRepository, ProductionOrderOperationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'production_order_operations'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ProductionOrderOperation}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'production_order_operations'.</p>
 *
 * <p><b>Database Table   :</b> {@code production_order_operations}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "production_order_operations")
public class ProductionOrderOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_order_id", nullable = false)
    private ProductionOrder productionOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routing_operation_id")
    private RoutingOperation routingOperation;

    @Column(name = "operation_number", nullable = false)
    private Integer operationNumber;

    @Column(name = "operation_code", nullable = false, length = 50)
    private String operationCode;

    @Column(length = 255)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_center_id", nullable = false)
    private WorkCenter workCenter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id")
    private Machine machine;

    @Column(nullable = false, length = 30)
    private String status = "PENDING"; // PENDING, QUEUED, SETUP, IN_PROGRESS, COMPLETED, SKIPPED, SCRAPPED

    @Column(name = "planned_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal plannedQuantity;

    @Column(name = "completed_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal completedQuantity = BigDecimal.ZERO;

    @Column(name = "scrapped_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal scrappedQuantity = BigDecimal.ZERO;

    @Column(name = "planned_setup_hours", nullable = false, precision = 10, scale = 4)
    private BigDecimal plannedSetupHours = BigDecimal.ZERO;

    @Column(name = "actual_setup_hours", nullable = false, precision = 10, scale = 4)
    private BigDecimal actualSetupHours = BigDecimal.ZERO;

    @Column(name = "planned_run_hours", nullable = false, precision = 10, scale = 4)
    private BigDecimal plannedRunHours = BigDecimal.ZERO;

    @Column(name = "actual_run_hours", nullable = false, precision = 10, scale = 4)
    private BigDecimal actualRunHours = BigDecimal.ZERO;

    @Column(name = "planned_start_datetime")
    private LocalDateTime plannedStartDatetime;

    @Column(name = "planned_end_datetime")
    private LocalDateTime plannedEndDatetime;

    @Column(name = "actual_start_datetime")
    private LocalDateTime actualStartDatetime;

    @Column(name = "actual_end_datetime")
    private LocalDateTime actualEndDatetime;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ProductionOrderOperation() {}

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
     * Retrieves production order data from the database.
     *
     * @return the ProductionOrder result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public ProductionOrder getProductionOrder() { return productionOrder; }
    /**
     * Performs the setProductionOrder operation in this module.
     *
     * @param productionOrder the productionOrder input value
     */
    public void setProductionOrder(ProductionOrder productionOrder) { this.productionOrder = productionOrder; }
    /**
     * Retrieves routing operation data from the database.
     *
     * @return the RoutingOperation result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public RoutingOperation getRoutingOperation() { return routingOperation; }
    /**
     * Performs the setRoutingOperation operation in this module.
     *
     * @param routingOperation the routingOperation input value
     */
    public void setRoutingOperation(RoutingOperation routingOperation) { this.routingOperation = routingOperation; }
    /**
     * Retrieves operation number data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getOperationNumber() { return operationNumber; }
    /**
     * Performs the setOperationNumber operation in this module.
     *
     * @param operationNumber the operationNumber input value
     */
    public void setOperationNumber(Integer operationNumber) { this.operationNumber = operationNumber; }
    /**
     * Retrieves operation code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOperationCode() { return operationCode; }
    /**
     * Performs the setOperationCode operation in this module.
     *
     * @param operationCode the operationCode input value
     */
    public void setOperationCode(String operationCode) { this.operationCode = operationCode; }
    /**
     * Retrieves description data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDescription() { return description; }
    /**
     * Performs the setDescription operation in this module.
     *
     * @param description the description input value
     */
    public void setDescription(String description) { this.description = description; }
    /**
     * Retrieves work center data from the database.
     *
     * @return the WorkCenter result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public WorkCenter getWorkCenter() { return workCenter; }
    /**
     * Performs the setWorkCenter operation in this module.
     *
     * @param workCenter the workCenter input value
     */
    public void setWorkCenter(WorkCenter workCenter) { this.workCenter = workCenter; }
    /**
     * Retrieves machine data from the database.
     *
     * @return the Machine result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Machine getMachine() { return machine; }
    /**
     * Performs the setMachine operation in this module.
     *
     * @param machine the machine input value
     */
    public void setMachine(Machine machine) { this.machine = machine; }
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
     * Retrieves planned quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getPlannedQuantity() { return plannedQuantity; }
    /**
     * Performs the setPlannedQuantity operation in this module.
     *
     * @param plannedQuantity the plannedQuantity input value
     */
    public void setPlannedQuantity(BigDecimal plannedQuantity) { this.plannedQuantity = plannedQuantity; }
    /**
     * Retrieves completed quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCompletedQuantity() { return completedQuantity; }
    /**
     * Performs the setCompletedQuantity operation in this module.
     *
     * @param completedQuantity the completedQuantity input value
     */
    public void setCompletedQuantity(BigDecimal completedQuantity) { this.completedQuantity = completedQuantity; }
    /**
     * Retrieves scrapped quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getScrappedQuantity() { return scrappedQuantity; }
    /**
     * Performs the setScrappedQuantity operation in this module.
     *
     * @param scrappedQuantity the scrappedQuantity input value
     */
    public void setScrappedQuantity(BigDecimal scrappedQuantity) { this.scrappedQuantity = scrappedQuantity; }
    /**
     * Retrieves planned setup hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getPlannedSetupHours() { return plannedSetupHours; }
    /**
     * Performs the setPlannedSetupHours operation in this module.
     *
     * @param plannedSetupHours the plannedSetupHours input value
     */
    public void setPlannedSetupHours(BigDecimal plannedSetupHours) { this.plannedSetupHours = plannedSetupHours; }
    /**
     * Retrieves actual setup hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getActualSetupHours() { return actualSetupHours; }
    /**
     * Performs the setActualSetupHours operation in this module.
     *
     * @param actualSetupHours the actualSetupHours input value
     */
    public void setActualSetupHours(BigDecimal actualSetupHours) { this.actualSetupHours = actualSetupHours; }
    /**
     * Retrieves planned run hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getPlannedRunHours() { return plannedRunHours; }
    /**
     * Performs the setPlannedRunHours operation in this module.
     *
     * @param plannedRunHours the plannedRunHours input value
     */
    public void setPlannedRunHours(BigDecimal plannedRunHours) { this.plannedRunHours = plannedRunHours; }
    /**
     * Retrieves actual run hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getActualRunHours() { return actualRunHours; }
    /**
     * Performs the setActualRunHours operation in this module.
     *
     * @param actualRunHours the actualRunHours input value
     */
    public void setActualRunHours(BigDecimal actualRunHours) { this.actualRunHours = actualRunHours; }
    /**
     * Retrieves planned start datetime data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getPlannedStartDatetime() { return plannedStartDatetime; }
    /**
     * Performs the setPlannedStartDatetime operation in this module.
     *
     * @param plannedStartDatetime the plannedStartDatetime input value
     */
    public void setPlannedStartDatetime(LocalDateTime plannedStartDatetime) { this.plannedStartDatetime = plannedStartDatetime; }
    /**
     * Retrieves planned end datetime data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getPlannedEndDatetime() { return plannedEndDatetime; }
    /**
     * Performs the setPlannedEndDatetime operation in this module.
     *
     * @param plannedEndDatetime the plannedEndDatetime input value
     */
    public void setPlannedEndDatetime(LocalDateTime plannedEndDatetime) { this.plannedEndDatetime = plannedEndDatetime; }
    /**
     * Retrieves actual start datetime data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getActualStartDatetime() { return actualStartDatetime; }
    /**
     * Performs the setActualStartDatetime operation in this module.
     *
     * @param actualStartDatetime the actualStartDatetime input value
     */
    public void setActualStartDatetime(LocalDateTime actualStartDatetime) { this.actualStartDatetime = actualStartDatetime; }
    /**
     * Retrieves actual end datetime data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getActualEndDatetime() { return actualEndDatetime; }
    /**
     * Performs the setActualEndDatetime operation in this module.
     *
     * @param actualEndDatetime the actualEndDatetime input value
     */
    public void setActualEndDatetime(LocalDateTime actualEndDatetime) { this.actualEndDatetime = actualEndDatetime; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}