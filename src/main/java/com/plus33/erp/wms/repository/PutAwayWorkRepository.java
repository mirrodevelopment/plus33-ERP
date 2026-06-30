package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.PutAwayWork;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PutAwayWorkRepository extends JpaRepository<PutAwayWork, Long> {
    List<PutAwayWork> findByAsnId(Long asnId);
    List<PutAwayWork> findByCompanyIdAndWarehouseIdAndStatus(Long companyId, Long warehouseId, String status);
    List<PutAwayWork> findByAssignedToAndStatus(Long userId, String status);
}
