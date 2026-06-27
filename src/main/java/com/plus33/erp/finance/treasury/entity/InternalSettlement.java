package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "internal_settlements")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InternalSettlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_ihb_account_id", nullable = false)
    private InHouseBankAccount sourceIhbAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_ihb_account_id", nullable = false)
    private InHouseBankAccount targetIhbAccount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "settlement_date", nullable = false)
    private LocalDate settlementDate;

    @Column(name = "reference_number", nullable = false, length = 100)
    private String referenceNumber;

    @Column(length = 255)
    private String notes;
}
