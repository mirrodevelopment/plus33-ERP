package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.inventory.entity.UnitOfMeasure;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ProductionOrder getProductionOrder() { return productionOrder; }
    public void setProductionOrder(ProductionOrder productionOrder) { this.productionOrder = productionOrder; }
    public ProductionOrderOperation getProductionOrderOperation() { return productionOrderOperation; }
    public void setProductionOrderOperation(ProductionOrderOperation poo) { this.productionOrderOperation = poo; }
    public String getConfirmationNumber() { return confirmationNumber; }
    public void setConfirmationNumber(String confirmationNumber) { this.confirmationNumber = confirmationNumber; }
    public String getConfirmationType() { return confirmationType; }
    public void setConfirmationType(String confirmationType) { this.confirmationType = confirmationType; }
    public BigDecimal getConfirmedQuantity() { return confirmedQuantity; }
    public void setConfirmedQuantity(BigDecimal confirmedQuantity) { this.confirmedQuantity = confirmedQuantity; }
    public BigDecimal getScrappedQuantity() { return scrappedQuantity; }
    public void setScrappedQuantity(BigDecimal scrappedQuantity) { this.scrappedQuantity = scrappedQuantity; }
    public BigDecimal getReworkQuantity() { return reworkQuantity; }
    public void setReworkQuantity(BigDecimal reworkQuantity) { this.reworkQuantity = reworkQuantity; }
    public UnitOfMeasure getUnit() { return unit; }
    public void setUnit(UnitOfMeasure unit) { this.unit = unit; }
    public BigDecimal getActualLaborHours() { return actualLaborHours; }
    public void setActualLaborHours(BigDecimal actualLaborHours) { this.actualLaborHours = actualLaborHours; }
    public BigDecimal getActualMachineHours() { return actualMachineHours; }
    public void setActualMachineHours(BigDecimal actualMachineHours) { this.actualMachineHours = actualMachineHours; }
    public Long getLaborGroupId() { return laborGroupId; }
    public void setLaborGroupId(Long laborGroupId) { this.laborGroupId = laborGroupId; }
    public Machine getMachine() { return machine; }
    public void setMachine(Machine machine) { this.machine = machine; }
    public WorkCenter getWorkCenter() { return workCenter; }
    public void setWorkCenter(WorkCenter workCenter) { this.workCenter = workCenter; }
    public Boolean getFinishedGoodsReceived() { return finishedGoodsReceived; }
    public void setFinishedGoodsReceived(Boolean finishedGoodsReceived) { this.finishedGoodsReceived = finishedGoodsReceived; }
    public Long getFgWarehouseId() { return fgWarehouseId; }
    public void setFgWarehouseId(Long fgWarehouseId) { this.fgWarehouseId = fgWarehouseId; }
    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    public Long getJournalEntryId() { return journalEntryId; }
    public void setJournalEntryId(Long journalEntryId) { this.journalEntryId = journalEntryId; }
    public Long getConfirmedBy() { return confirmedBy; }
    public void setConfirmedBy(Long confirmedBy) { this.confirmedBy = confirmedBy; }
    public LocalDateTime getConfirmedAt() { return confirmedAt; }
    public void setConfirmedAt(LocalDateTime confirmedAt) { this.confirmedAt = confirmedAt; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
