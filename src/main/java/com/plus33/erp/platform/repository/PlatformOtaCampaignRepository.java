package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformOtaCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformOtaCampaignRepository extends JpaRepository<PlatformOtaCampaign, Long> {
}