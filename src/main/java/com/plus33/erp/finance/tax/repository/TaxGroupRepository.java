package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxGroupRepository extends JpaRepository<TaxGroup, Long> {
    Optional<TaxGroup> findByCode(String code);
}
