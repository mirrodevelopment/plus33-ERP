/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.entity
 * File              : CostCenter.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CostCenterController
 * Related Service   : CostCenterService, CostCenterServiceImpl
 * Related Repository: CostCenterRepository
 * Related Entity    : CostCenter
 * Related DTO       : N/A
 * Related Mapper    : CostCenterMapper
 * Related DB Table  : cost_centers
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : CostCenterRepository, CostCenterMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'cost_centers'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.budget.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "cost_centers", uniqueConstraints = {
    @UniqueConstraint(name = "uk_cost_centers_company_code", columnNames = {"company_id", "code"})
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code CostCenter}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'cost_centers'.</p>
 *
 * <p><b>Database Table   :</b> {@code cost_centers}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}