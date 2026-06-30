package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.LotGenealogy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LotGenealogyRepository extends JpaRepository<LotGenealogy, Long> {
    List<LotGenealogy> findByCompanyIdAndParentLotNumber(Long companyId, String parentLotNumber);
    List<LotGenealogy> findByCompanyIdAndChildLotNumber(Long companyId, String childLotNumber);
}
