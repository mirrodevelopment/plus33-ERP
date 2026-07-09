/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : MrpRun.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MrpRunController
 * Related Service   : MrpRunService, MrpRunServiceImpl
 * Related Repository: MrpRunRepository
 * Related Entity    : MrpRun
 * Related DTO       : N/A
 * Related Mapper    : MrpRunMapper
 * Related DB Table  : mrp_runs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MrpRunRepository, MrpRunMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'mrp_runs'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code MrpRun}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'mrp_runs'.</p>
 *
 * <p><b>Database Table   :</b> {@code mrp_runs}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "mrp_runs")
public class MrpRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "run_number", nullable = false, length = 50)
    private String runNumber;

    @Column(name = "run_type", nullable = false, length = 30)
    private String runType = "REGENERATIVE"; // REGENERATIVE, NET_CHANGE, NET_CHANGE_PLANNED

    @Column(nullable = false, length = 30)
    private String status = "PENDING"; // PENDING, RUNNING, COMPLETED, FAILED, CANCELLED

    @Column(name = "planning_horizon_days", nullable = false)
    private Integer planningHorizonDays = 90;

    @Column(name = "include_forecasts", nullable = false)
    private Boolean includeForecasts = true;

    @Column(name = "include_sales_orders", nullable = false)
    private Boolean includeSalesOrders = true;

    @Column(name = "include_safety_stock", nullable = false)
    private Boolean includeSafetyStock = true;

    @Column(name = "planned_orders_generated", nullable = false)
    private Integer plannedOrdersGenerated = 0;

    @Column(name = "purchase_reqs_generated", nullable = false)
    private Integer purchaseReqsGenerated = 0;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "run_by", nullable = false)
    private Long runBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public MrpRun() {}

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
     * Retrieves run number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRunNumber() { return runNumber; }
    /**
     * Performs the setRunNumber operation in this module.
     *
     * @param runNumber the runNumber input value
     */
    public void setRunNumber(String runNumber) { this.runNumber = runNumber; }
    /**
     * Retrieves run type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRunType() { return runType; }
    /**
     * Performs the setRunType operation in this module.
     *
     * @param runType the runType input value
     */
    public void setRunType(String runType) { this.runType = runType; }
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
     * Retrieves planning horizon days data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getPlanningHorizonDays() { return planningHorizonDays; }
    /**
     * Performs the setPlanningHorizonDays operation in this module.
     *
     * @param planningHorizonDays the planningHorizonDays input value
     */
    public void setPlanningHorizonDays(Integer planningHorizonDays) { this.planningHorizonDays = planningHorizonDays; }
    /**
     * Retrieves include forecasts data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getIncludeForecasts() { return includeForecasts; }
    /**
     * Performs the setIncludeForecasts operation in this module.
     *
     * @param includeForecasts the includeForecasts input value
     */
    public void setIncludeForecasts(Boolean includeForecasts) { this.includeForecasts = includeForecasts; }
    /**
     * Retrieves include sales orders data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getIncludeSalesOrders() { return includeSalesOrders; }
    /**
     * Performs the setIncludeSalesOrders operation in this module.
     *
     * @param includeSalesOrders the includeSalesOrders input value
     */
    public void setIncludeSalesOrders(Boolean includeSalesOrders) { this.includeSalesOrders = includeSalesOrders; }
    /**
     * Retrieves include safety stock data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getIncludeSafetyStock() { return includeSafetyStock; }
    /**
     * Performs the setIncludeSafetyStock operation in this module.
     *
     * @param includeSafetyStock the includeSafetyStock input value
     */
    public void setIncludeSafetyStock(Boolean includeSafetyStock) { this.includeSafetyStock = includeSafetyStock; }
    /**
     * Retrieves planned orders generated data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getPlannedOrdersGenerated() { return plannedOrdersGenerated; }
    /**
     * Performs the setPlannedOrdersGenerated operation in this module.
     *
     * @param plannedOrdersGenerated the plannedOrdersGenerated input value
     */
    public void setPlannedOrdersGenerated(Integer plannedOrdersGenerated) { this.plannedOrdersGenerated = plannedOrdersGenerated; }
    /**
     * Retrieves purchase reqs generated data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getPurchaseReqsGenerated() { return purchaseReqsGenerated; }
    /**
     * Performs the setPurchaseReqsGenerated operation in this module.
     *
     * @param purchaseReqsGenerated the purchaseReqsGenerated input value
     */
    public void setPurchaseReqsGenerated(Integer purchaseReqsGenerated) { this.purchaseReqsGenerated = purchaseReqsGenerated; }
    /**
     * Retrieves started at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getStartedAt() { return startedAt; }
    /**
     * Performs the setStartedAt operation in this module.
     *
     * @param startedAt the startedAt input value
     */
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    /**
     * Retrieves completed at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCompletedAt() { return completedAt; }
    /**
     * Performs the setCompletedAt operation in this module.
     *
     * @param completedAt the completedAt input value
     */
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    /**
     * Retrieves error message data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getErrorMessage() { return errorMessage; }
    /**
     * Performs the setErrorMessage operation in this module.
     *
     * @param errorMessage the errorMessage input value
     */
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    /**
     * Retrieves run by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getRunBy() { return runBy; }
    /**
     * Performs the setRunBy operation in this module.
     *
     * @param runBy the runBy input value
     */
    public void setRunBy(Long runBy) { this.runBy = runBy; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Getter/setter aliases for compatibility
    /**
     * Retrieves executed by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getExecutedBy() { return runBy; }
    /**
     * Performs the setExecutedBy operation in this module.
     *
     * @param executedBy the executedBy input value
     */
    public void setExecutedBy(Long executedBy) { this.runBy = executedBy; }
    /**
     * Retrieves items processed data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getItemsProcessed() { return plannedOrdersGenerated; }
    /**
     * Performs the setItemsProcessed operation in this module.
     *
     * @param itemsProcessed the itemsProcessed input value
     */
    public void setItemsProcessed(Integer itemsProcessed) { this.plannedOrdersGenerated = itemsProcessed; }
    /**
     * Retrieves orders generated data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getOrdersGenerated() { return plannedOrdersGenerated; }
    /**
     * Performs the setOrdersGenerated operation in this module.
     *
     * @param ordersGenerated the ordersGenerated input value
     */
    public void setOrdersGenerated(Integer ordersGenerated) { this.plannedOrdersGenerated = ordersGenerated; }
}