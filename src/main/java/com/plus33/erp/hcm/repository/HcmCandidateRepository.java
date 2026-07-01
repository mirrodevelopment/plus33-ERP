package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.HcmCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HcmCandidateRepository extends JpaRepository<HcmCandidate, Long> {
    List<HcmCandidate> findByRequisitionId(Long requisitionId);
}
