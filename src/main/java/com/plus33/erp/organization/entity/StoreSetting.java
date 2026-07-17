package com.plus33.erp.organization.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JPA Entity mapped to 'store_settings'. Defines persistent configuration options for a store location.
 */
@Getter
@Setter
@Entity
@Table(name = "store_settings")
public class StoreSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false, unique = true)
    private Store store;

    @Column(name = "operating_hours", nullable = false, length = 100)
    private String operatingHours = "08:00 - 22:00";

    @Column(name = "wifi_ssid", nullable = false, length = 100)
    private String wifiSsid = "PLUS33-Guest";

    @Column(name = "wifi_password", nullable = false, length = 100)
    private String wifiPassword = "CoffeeBreak";

    @Column(name = "low_stock_threshold", nullable = false)
    private Integer lowStockThreshold = 50;

    @Column(name = "sales_target", nullable = false, precision = 12, scale = 2)
    private BigDecimal salesTarget = new BigDecimal("10000.00");

    @Column(name = "receipt_footer", nullable = false, columnDefinition = "TEXT")
    private String receiptFooter = "Thank you for visiting PLUS33 Coffee!";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
