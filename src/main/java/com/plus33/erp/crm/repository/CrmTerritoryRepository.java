package com.plus33.erp.crm.repository;

import com.plus33.erp.crm.entity.CrmTerritory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CrmTerritoryRepository extends JpaRepository<CrmTerritory, Long> {
    List<CrmTerritory> findByCompanyId(Long companyId);
}
