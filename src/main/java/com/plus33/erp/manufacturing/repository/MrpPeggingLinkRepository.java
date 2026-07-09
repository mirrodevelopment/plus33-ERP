/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : MrpPeggingLinkRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MrpPeggingLinkController
 * Related Service   : MrpPeggingLinkService, MrpPeggingLinkServiceImpl
 * Related Repository: MrpPeggingLinkRepository
 * Related Entity    : MrpPeggingLink
 * Related DTO       : N/A
 * Related Mapper    : MrpPeggingLinkMapper
 * Related DB Table  : mrp_pegging_links
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MrpPeggingLinkService, MrpPeggingLinkServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'mrp_pegging_links' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.MrpPeggingLink;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code MrpPeggingLinkRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'mrp_pegging_links' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code mrp_pegging_links}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface MrpPeggingLinkRepository extends JpaRepository<MrpPeggingLink, Long> {
    List<MrpPeggingLink> findByMrpRunId(Long mrpRunId);
    List<MrpPeggingLink> findByCompanyId(Long companyId);
    List<MrpPeggingLink> findByDemandTypeAndDemandId(String demandType, Long demandId);
    List<MrpPeggingLink> findBySupplyTypeAndSupplyId(String supplyType, Long supplyId);
}
