/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : LotGenealogyRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: LotGenealogyController
 * Related Service   : LotGenealogyService, LotGenealogyServiceImpl
 * Related Repository: LotGenealogyRepository
 * Related Entity    : LotGenealogy
 * Related DTO       : N/A
 * Related Mapper    : LotGenealogyMapper
 * Related DB Table  : lot_genealogys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : LotGenealogyService, LotGenealogyServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'lot_genealogys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.LotGenealogy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code LotGenealogyRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'lot_genealogys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code lot_genealogys}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface LotGenealogyRepository extends JpaRepository<LotGenealogy, Long> {
    List<LotGenealogy> findByCompanyIdAndParentLotNumber(Long companyId, String parentLotNumber);
    List<LotGenealogy> findByCompanyIdAndChildLotNumber(Long companyId, String childLotNumber);
}
