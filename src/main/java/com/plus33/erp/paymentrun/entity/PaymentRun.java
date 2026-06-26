package com.plus33.erp.paymentrun.entity;

import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.procurement.entity.Supplier;
import com.plus33.erp.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "payment_runs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "run_number", nullable = false, unique = true, length = 50)
    private String runNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private PaymentRunStatus status = PaymentRunStatus.DRAFT;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "payment_method", nullable = false, length = 30)
    private String paymentMethod;

    @Column(name = "currency_code", nullable = false, length = 3)
    @Builder.Default
    private String currencyCode = "AED";

    @Column(name = "filter_due_date")
    private LocalDate filterDueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filter_supplier_id")
    private Supplier filterSupplier;

    @Column(name = "bank_account_code", nullable = false, length = 20)
    @Builder.Default
    private String bankAccountCode = "1200";

    @Column(name = "total_amount", nullable = false, precision = 14, scale = 2)
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "export_format", nullable = false, length = 30)
    @Builder.Default
    private String exportFormat = "CSV";

    @Column(name = "export_file_name")
    private String exportFileName;

    @Column(name = "export_storage_path")
    private String exportStoragePath;

    @Column(name = "export_checksum")
    private String exportChecksum;

    @Column(name = "export_generated_at")
    private LocalDateTime exportGeneratedAt;


    @Column(name = "client_reference_id", unique = true)
    private UUID clientReferenceId;

    // Execution audit metrics
    @Column(name = "successful_payments_count", nullable = false)
    @Builder.Default
    private Integer successfulPaymentsCount = 0;

    @Column(name = "failed_payments_count", nullable = false)
    @Builder.Default
    private Integer failedPaymentsCount = 0;

    @Column(name = "processed_invoices_count", nullable = false)
    @Builder.Default
    private Integer processedInvoicesCount = 0;

    @Column(name = "failure_reason", columnDefinition = "TEXT")
    private String failureReason;

    // Status audit trail
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executed_by")
    private User executedBy;

    @Column(name = "executed_at")
    private LocalDateTime executedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_by")
    private User cancelledBy;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    // Core audit fields
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    @OneToMany(mappedBy = "paymentRun", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PaymentRunInvoice> invoices = new ArrayList<>();

    @OneToMany(mappedBy = "paymentRun", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PaymentRunSupplierResult> supplierResults = new ArrayList<>();


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
