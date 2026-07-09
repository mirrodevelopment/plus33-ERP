/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : BankBranch.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BankBranchController
 * Related Service   : BankBranchService, BankBranchServiceImpl
 * Related Repository: BankBranchRepository
 * Related Entity    : BankBranch
 * Related DTO       : N/A
 * Related Mapper    : BankBranchMapper
 * Related DB Table  : bank_branches
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BankBranchRepository, BankBranchMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bank_branches'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "bank_branches", uniqueConstraints = {
    @UniqueConstraint(name = "uk_branches_bank_code", columnNames = {"bank_id", "code"})
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BankBranch}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bank_branches'.</p>
 *
 * <p><b>Database Table   :</b> {@code bank_branches}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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