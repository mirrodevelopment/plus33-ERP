package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "robot_tasks")
public class RobotTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id")
    private EquipmentAsset equipment;

    @Column(name = "warehouse_task_id", nullable = false)
    private Long warehouseTaskId;

    @Column(name = "robot_provider_key", nullable = false, length = 50)
    private String robotProviderKey;

    @Column(name = "payload_json", columnDefinition = "TEXT")
    private String payloadJson;

    @Column(nullable = false, length = 30)
    private String status = "DISPATCHED";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public EquipmentAsset getEquipment() { return equipment; }
    public void setEquipment(EquipmentAsset equipment) { this.equipment = equipment; }
    public Long getWarehouseTaskId() { return warehouseTaskId; }
    public void setWarehouseTaskId(Long warehouseTaskId) { this.warehouseTaskId = warehouseTaskId; }
    public String getRobotProviderKey() { return robotProviderKey; }
    public void setRobotProviderKey(String robotProviderKey) { this.robotProviderKey = robotProviderKey; }
    public String getPayloadJson() { return payloadJson; }
    public void setPayloadJson(String payloadJson) { this.payloadJson = payloadJson; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
