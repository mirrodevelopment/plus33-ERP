package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.TreasuryAccountingProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TreasuryAccountingProfileRepository extends JpaRepository<TreasuryAccountingProfile, Long> {

    Optional<TreasuryAccountingProfile> findByCompanyIdAndProfileCode(Long companyId, String profileCode);

    Optional<TreasuryAccountingProfile> findFirstByCompanyIdOrderByProfileCodeAsc(Long companyId);
}
