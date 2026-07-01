package com.plus33.erp.crm.service;

import com.plus33.erp.crm.entity.CrmTerritory;
import com.plus33.erp.crm.repository.CrmTerritoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerritoryAssignmentEngine {

    private final CrmTerritoryRepository territoryRepo;

    public TerritoryAssignmentEngine(CrmTerritoryRepository territoryRepo) {
        this.territoryRepo = territoryRepo;
    }

    public CrmTerritory assignTerritory(Long companyId, String region, String postalCode) {
        List<CrmTerritory> territories = territoryRepo.findByCompanyId(companyId);
        return territories.stream()
                .filter(t -> t.getRegionName().equalsIgnoreCase(region) || 
                             (t.getPostalCodeRange() != null && postalCode.startsWith(t.getPostalCodeRange())))
                .findFirst()
                .orElse(null);
    }
}
