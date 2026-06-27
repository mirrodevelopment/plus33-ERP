package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxCategoryRepository extends JpaRepository<TaxCategory, Long> {
    Optional<TaxCategory> findByCode(String code);
}
