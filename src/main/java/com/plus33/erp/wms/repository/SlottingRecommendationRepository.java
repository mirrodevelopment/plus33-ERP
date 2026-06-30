package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.SlottingRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SlottingRecommendationRepository extends JpaRepository<SlottingRecommendation, Long> {
    List<SlottingRecommendation> findByCompanyIdAndWarehouseIdAndStatus(Long companyId, Long warehouseId, String status);
}
