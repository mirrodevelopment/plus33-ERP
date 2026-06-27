package com.plus33.erp.finance.treasury.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "bank_branches", uniqueConstraints = {
    @UniqueConstraint(name = "uk_branches_bank_code", columnNames = {"bank_id", "code"})
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "swift_code", nullable = false, length = 30)
    private String swiftCode;

    @Column(length = 255)
    private String address;
}
