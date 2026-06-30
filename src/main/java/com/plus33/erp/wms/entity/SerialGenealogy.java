package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "serial_genealogy")
public class SerialGenealogy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "parent_serial_number", length = 100)
    private String parentSerialNumber;

    @Column(name = "child_serial_number", nullable = false, length = 100)
    private String childSerialNumber;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "location_id")
    private Long locationId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getParentSerialNumber() { return parentSerialNumber; }
    public void setParentSerialNumber(String parentSerialNumber) { this.parentSerialNumber = parentSerialNumber; }
    public String getChildSerialNumber() { return childSerialNumber; }
    public void setChildSerialNumber(String childSerialNumber) { this.childSerialNumber = childSerialNumber; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
