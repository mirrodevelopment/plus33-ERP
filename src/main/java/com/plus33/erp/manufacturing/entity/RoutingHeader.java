package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.UnitOfMeasure;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "routing_headers")
public class RoutingHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "routing_number", nullable = false, length = 50)
    private String routingNumber;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, length = 20)
    private String revision = "00";

    @Column(nullable = false, length = 30)
    private String status = "DRAFT"; // DRAFT, ACTIVE, SUPERSEDED, OBSOLETE

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "lead_time_hours", precision = 10, scale = 2)
    private BigDecimal leadTimeHours;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @OneToMany(mappedBy = "routingHeader", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("operationNumber ASC")
    private java.util.List<RoutingOperation> operations = new java.util.ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public RoutingHeader() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public String getRoutingNumber() { return routingNumber; }
    public void setRoutingNumber(String routingNumber) { this.routingNumber = routingNumber; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRevision() { return revision; }
    public void setRevision(String revision) { this.revision = revision; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    public LocalDate getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }
    public BigDecimal getLeadTimeHours() { return leadTimeHours; }
    public void setLeadTimeHours(BigDecimal leadTimeHours) { this.leadTimeHours = leadTimeHours; }
    public Long getApprovedBy() { return approvedBy; }
    public void setApprovedBy(Long approvedBy) { this.approvedBy = approvedBy; }
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    public java.util.List<RoutingOperation> getOperations() { return operations; }
    public void setOperations(java.util.List<RoutingOperation> operations) { this.operations = operations; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
