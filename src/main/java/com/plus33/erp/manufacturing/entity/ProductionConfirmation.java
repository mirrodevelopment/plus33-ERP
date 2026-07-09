/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : ProductionConfirmation.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionConfirmationController
 * Related Service   : ProductionConfirmationService, ProductionConfirmationServiceImpl
 * Related Repository: ProductionConfirmationRepository
 * Related Entity    : ProductionConfirmation
 * Related DTO       : N/A
 * Related Mapper    : ProductionConfirmationMapper
 * Related DB Table  : production_confirmations
 * Related REST APIs : N/A
 * Depends On        : Inventory Module
 * Used By           : ProductionConfirmationRepository, ProductionConfirmationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'production_confirmations'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.inventory.entity.UnitOfMeasure;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ProductionConfirmation}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'production_confirmations'.</p>
 *
 * <p><b>Database Table   :</b> {@code production_confirmations}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "production_confirmations")
public class ProductionConfirmation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_order_id", nullable = false)
    private ProductionOrder productionOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_order_operation_id")
    private ProductionOrderOperation productionOrderOperation;

    @Column(name = "confirmation_number", nullable = false, length = 50)
    private String confirmationNumber;

    @Column(name = "confirmation_type", nullable = false, length = 30)
    private String confirmationType = "PARTIAL"; // PARTIAL, FINAL, REVERSAL

    @Column(name = "confirmed_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal confirmedQuantity;

    @Column(name = "scrapped_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal scrappedQuantity = BigDecimal.ZERO;

    @Column(name = "rework_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal reworkQuantity = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitOfMeasure unit;

    @Column(name = "actual_labor_hours", nullable = false, precision = 10, scale = 4)
    private BigDecimal actualLaborHours = BigDecimal.ZERO;

    @Column(name = "actual_machine_hours", nullable = false, precision = 10, scale = 4)
    private BigDecimal actualMachineHours = BigDecimal.ZERO;

    @Column(name = "labor_group_id")
    private Long laborGroupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id")
    private Machine machine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_center_id", nullable = false)
    private WorkCenter workCenter;

    @Column(name = "finished_goods_received", nullable = false)
    private Boolean finishedGoodsReceived = false;

    @Column(name = "fg_warehouse_id")
    private Long fgWarehouseId;

    @Column(name = "lot_number", length = 100)
    private String lotNumber;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "journal_entry_id")
    private Long journalEntryId;

    @Column(name = "confirmed_by", nullable = false)
    private Long confirmedBy;

    @Column(name = "confirmed_at", nullable = false)
    private LocalDateTime confirmedAt = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ProductionConfirmation() {}

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
     * Retrieves production order operation data from the database.
     *
     * @return the ProductionOrderOperation result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public ProductionOrderOperation getProductionOrderOperation() { return productionOrderOperation; }
    /**
     * Performs the setProductionOrderOperation operation in this module.
     *
     * @param poo the poo input value
     */
    public void setProductionOrderOperation(ProductionOrderOperation poo) { this.productionOrderOperation = poo; }
    /**
     * Retrieves confirmation number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getConfirmationNumber() { return confirmationNumber; }
    /**
     * Performs the setConfirmationNumber operation in this module.
     *
     * @param confirmationNumber the confirmationNumber input value
     */
    public void setConfirmationNumber(String confirmationNumber) { this.confirmationNumber = confirmationNumber; }
    /**
     * Retrieves confirmation type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getConfirmationType() { return confirmationType; }
    /**
     * Performs the setConfirmationType operation in this module.
     *
     * @param confirmationType the confirmationType input value
     */
    public void setConfirmationType(String confirmationType) { this.confirmationType = confirmationType; }
    /**
     * Retrieves confirmed quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getConfirmedQuantity() { return confirmedQuantity; }
    /**
     * Performs the setConfirmedQuantity operation in this module.
     *
     * @param confirmedQuantity the confirmedQuantity input value
     */
    public void setConfirmedQuantity(BigDecimal confirmedQuantity) { this.confirmedQuantity = confirmedQuantity; }
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
     * Retrieves rework quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getReworkQuantity() { return reworkQuantity; }
    /**
     * Performs the setReworkQuantity operation in this module.
     *
     * @param reworkQuantity the reworkQuantity input value
     */
    public void setReworkQuantity(BigDecimal reworkQuantity) { this.reworkQuantity = reworkQuantity; }
    /**
     * Retrieves unit data from the database.
     *
     * @return the UnitOfMeasure result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public UnitOfMeasure getUnit() { return unit; }
    /**
     * Performs the setUnit operation in this module.
     *
     * @param unit the unit input value
     */
    public void setUnit(UnitOfMeasure unit) { this.unit = unit; }
    /**
     * Retrieves a paginated list of actual labor hours records.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getActualLaborHours() { return actualLaborHours; }
    /**
     * Performs the setActualLaborHours operation in this module.
     *
     * @param actualLaborHours the actualLaborHours input value
     */
    public void setActualLaborHours(BigDecimal actualLaborHours) { this.actualLaborHours = actualLaborHours; }
    /**
     * Retrieves actual machine hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getActualMachineHours() { return actualMachineHours; }
    /**
     * Performs the setActualMachineHours operation in this module.
     *
     * @param actualMachineHours the actualMachineHours input value
     */
    public void setActualMachineHours(BigDecimal actualMachineHours) { this.actualMachineHours = actualMachineHours; }
    /**
     * Retrieves labor group id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getLaborGroupId() { return laborGroupId; }
    /**
     * Performs the setLaborGroupId operation in this module.
     *
     * @param laborGroupId the laborGroupId input value
     */
    public void setLaborGroupId(Long laborGroupId) { this.laborGroupId = laborGroupId; }
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
     * Retrieves finished goods received data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getFinishedGoodsReceived() { return finishedGoodsReceived; }
    /**
     * Performs the setFinishedGoodsReceived operation in this module.
     *
     * @param finishedGoodsReceived the finishedGoodsReceived input value
     */
    public void setFinishedGoodsReceived(Boolean finishedGoodsReceived) { this.finishedGoodsReceived = finishedGoodsReceived; }
    /**
     * Retrieves fg warehouse id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getFgWarehouseId() { return fgWarehouseId; }
    /**
     * Performs the setFgWarehouseId operation in this module.
     *
     * @param fgWarehouseId the fgWarehouseId input value
     */
    public void setFgWarehouseId(Long fgWarehouseId) { this.fgWarehouseId = fgWarehouseId; }
    /**
     * Retrieves lot number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getLotNumber() { return lotNumber; }
    /**
     * Performs the setLotNumber operation in this module.
     *
     * @param lotNumber the lotNumber input value
     */
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }
    /**
     * Retrieves serial number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSerialNumber() { return serialNumber; }
    /**
     * Performs the setSerialNumber operation in this module.
     *
     * @param serialNumber the serialNumber input value
     */
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    /**
     * Retrieves journal entry id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getJournalEntryId() { return journalEntryId; }
    /**
     * Performs the setJournalEntryId operation in this module.
     *
     * @param journalEntryId the journalEntryId input value
     */
    public void setJournalEntryId(Long journalEntryId) { this.journalEntryId = journalEntryId; }
    /**
     * Retrieves confirmed by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getConfirmedBy() { return confirmedBy; }
    /**
     * Performs the setConfirmedBy operation in this module.
     *
     * @param confirmedBy the confirmedBy input value
     */
    public void setConfirmedBy(Long confirmedBy) { this.confirmedBy = confirmedBy; }
    /**
     * Retrieves confirmed at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getConfirmedAt() { return confirmedAt; }
    /**
     * Performs the setConfirmedAt operation in this module.
     *
     * @param confirmedAt the confirmedAt input value
     */
    public void setConfirmedAt(LocalDateTime confirmedAt) { this.confirmedAt = confirmedAt; }
    /**
     * Retrieves notes data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNotes() { return notes; }
    /**
     * Performs the setNotes operation in this module.
     *
     * @param notes the notes input value
     */
    public void setNotes(String notes) { this.notes = notes; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}