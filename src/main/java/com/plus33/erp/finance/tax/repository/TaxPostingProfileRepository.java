package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxPostingProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxPostingProfileRepository extends JpaRepository<TaxPostingProfile, Long> {
    Optional<TaxPostingProfile> findByCompanyIdAndCategoryId(Long companyId, Long categoryId);
    Optional<TaxPostingProfile> findByCompanyIdAndCategoryCode(Long companyId, String categoryCode);
}
