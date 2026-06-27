package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxGroupLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxGroupLineRepository extends JpaRepository<TaxGroupLine, Long> {
    List<TaxGroupLine> findByGroupId(Long groupId);
}
