package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxAdjustmentEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxAdjustmentEntryRepository extends JpaRepository<TaxAdjustmentEntry, Long> {
    List<TaxAdjustmentEntry> findByCompanyId(Long companyId);
    List<TaxAdjustmentEntry> findByCompanyIdAndStatus(Long companyId, String status);
}
