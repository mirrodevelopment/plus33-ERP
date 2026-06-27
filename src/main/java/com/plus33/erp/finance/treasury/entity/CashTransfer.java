package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "cash_transfers")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_bank_account_id", nullable = false)
    private BankAccount sourceBankAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_bank_account_id", nullable = false)
    private BankAccount destinationBankAccount;

    @Column(name = "transfer_date", nullable = false)
    private LocalDate transferDate;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "exchange_rate", nullable = false, precision = 18, scale = 6)
    @Builder.Default
    private BigDecimal exchangeRate = BigDecimal.ONE;

    @Column(nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal fees = BigDecimal.ZERO;

    @Column(name = "reference_number", nullable = false, length = 100)
    private String referenceNumber;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "DRAFT"; // DRAFT, PENDING_APPROVAL, APPROVED, COMPLETED, CANCELLED

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "approved_by", length = 100)
    private String approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
