package com.plus33.erp.finance.tax.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "tax_group_lines")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxGroupLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private TaxGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rate_id", nullable = false)
    private TaxRate rate;
}
