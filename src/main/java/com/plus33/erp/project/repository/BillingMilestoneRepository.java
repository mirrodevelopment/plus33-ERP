package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.BillingMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BillingMilestoneRepository extends JpaRepository<BillingMilestone, Long> {
    List<BillingMilestone> findByContractId(Long contractId);
}
