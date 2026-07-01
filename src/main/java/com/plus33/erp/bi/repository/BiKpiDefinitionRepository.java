package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiKpiDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiKpiDefinitionRepository extends JpaRepository<BiKpiDefinition, Long> {
    java.util.List<BiKpiDefinition> findByStatus(String status);
    java.util.Optional<BiKpiDefinition> findByKpiCode(String kpiCode);
}
