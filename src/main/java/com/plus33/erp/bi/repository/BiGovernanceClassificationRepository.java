package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiGovernanceClassification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiGovernanceClassificationRepository extends JpaRepository<BiGovernanceClassification, Long> {
}