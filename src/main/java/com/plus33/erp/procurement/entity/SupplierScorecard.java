package com.plus33.erp.procurement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "procurement_supplier_scorecards")
public class SupplierScorecard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "supplier_id", nullable = false, unique = true)
    private Long supplierId;

    @Column(name = "on_time_delivery_rate", nullable = false)
    private BigDecimal onTimeDeliveryRate = new BigDecimal("100.00");

    @Column(name = "quality_defect_rate", nullable = false)
    private BigDecimal qualityDefectRate = BigDecimal.ZERO;

    @Column(name = "invoice_accuracy_rate", nullable = false)
    private BigDecimal invoiceAccuracyRate = new BigDecimal("100.00");

    @Column(name = "overall_rating", nullable = false)
    private BigDecimal overallRating = new BigDecimal("100.00");

    @Column(name = "recalculated_at", nullable = false)
    private LocalDateTime recalculatedAt = LocalDateTime.now();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    public BigDecimal getOnTimeDeliveryRate() { return onTimeDeliveryRate; }
    public void setOnTimeDeliveryRate(BigDecimal onTimeDeliveryRate) { this.onTimeDeliveryRate = onTimeDeliveryRate; }
    public BigDecimal getQualityDefectRate() { return qualityDefectRate; }
    public void setQualityDefectRate(BigDecimal qualityDefectRate) { this.qualityDefectRate = qualityDefectRate; }
    public BigDecimal getInvoiceAccuracyRate() { return invoiceAccuracyRate; }
    public void setInvoiceAccuracyRate(BigDecimal invoiceAccuracyRate) { this.invoiceAccuracyRate = invoiceAccuracyRate; }
    public BigDecimal getOverallRating() { return overallRating; }
    public void setOverallRating(BigDecimal overallRating) { this.overallRating = overallRating; }
    public LocalDateTime getRecalculatedAt() { return recalculatedAt; }
    public void setRecalculatedAt(LocalDateTime recalculatedAt) { this.recalculatedAt = recalculatedAt; }
}
