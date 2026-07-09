/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : AdvanceShippingNoticeRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AdvanceShippingNoticeController
 * Related Service   : AdvanceShippingNoticeService, AdvanceShippingNoticeServiceImpl
 * Related Repository: AdvanceShippingNoticeRepository
 * Related Entity    : AdvanceShippingNotice
 * Related DTO       : N/A
 * Related Mapper    : AdvanceShippingNoticeMapper
 * Related DB Table  : advance_shipping_notices
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AdvanceShippingNoticeService, AdvanceShippingNoticeServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'advance_shipping_notices' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.AdvanceShippingNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code AdvanceShippingNoticeRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'advance_shipping_notices' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code advance_shipping_notices}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface AdvanceShippingNoticeRepository extends JpaRepository<AdvanceShippingNotice, Long> {
    Optional<AdvanceShippingNotice> findByCompanyIdAndAsnNumber(Long companyId, String asnNumber);
    List<AdvanceShippingNotice> findByCompanyIdAndWarehouseIdAndStatus(Long companyId, Long warehouseId, String status);
    List<AdvanceShippingNotice> findBySupplierId(Long supplierId);
}
