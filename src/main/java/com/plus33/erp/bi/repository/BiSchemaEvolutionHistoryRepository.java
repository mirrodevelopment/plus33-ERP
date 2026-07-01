package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiSchemaEvolutionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiSchemaEvolutionHistoryRepository extends JpaRepository<BiSchemaEvolutionHistory, Long> {
}