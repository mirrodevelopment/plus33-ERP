package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.AdvanceShippingNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AdvanceShippingNoticeRepository extends JpaRepository<AdvanceShippingNotice, Long> {
    Optional<AdvanceShippingNotice> findByCompanyIdAndAsnNumber(Long companyId, String asnNumber);
    List<AdvanceShippingNotice> findByCompanyIdAndWarehouseIdAndStatus(Long companyId, Long warehouseId, String status);
    List<AdvanceShippingNotice> findBySupplierId(Long supplierId);
}
