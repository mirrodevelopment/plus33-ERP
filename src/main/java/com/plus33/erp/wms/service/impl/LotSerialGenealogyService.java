/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.service.impl
 * File              : LotSerialGenealogyService.java
 * Purpose           : Business logic service layer for Wms Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: LotSerialGenealogyController
 * Related Service   : LotSerialGenealogyService
 * Related Repository: LotGenealogyRepository
 * Related Entity    : LotSerialGenealogy
 * Related DTO       : N/A
 * Related Mapper    : LotSerialGenealogyMapper
 * Related DB Table  : lot_serial_genealogys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : LotSerialGenealogyController, LotSerialGenealogyServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Wms Module. Implements LotSerialGenealogyService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.wms.service.impl;

import com.plus33.erp.wms.entity.LotGenealogy;
import com.plus33.erp.wms.repository.LotGenealogyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code LotSerialGenealogyService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.service.impl}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Wms Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * LotSerialGenealogyController
 *   --> LotSerialGenealogyService (this)
 *   --> Validate business rules
 *   --> LotSerialGenealogyRepository (read/write 'lot_serial_genealogys')
 *   --> LotSerialGenealogyMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code lot_serial_genealogys}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional
public class LotSerialGenealogyService {

    private final LotGenealogyRepository genealogyRepo;

    public LotSerialGenealogyService(LotGenealogyRepository genealogyRepo) {
        this.genealogyRepo = genealogyRepo;
    }

    /**
     * Performs the recordLotSplit operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param parentLot the parentLot input value
     * @param childLot the childLot input value
     * @param productId the productId input value
     * @return the LotGenealogy result
     */
    public LotGenealogy recordLotSplit(Long companyId, String parentLot, String childLot, Long productId) {
        LotGenealogy g = new LotGenealogy();
        g.setCompanyId(companyId);
        g.setParentLotNumber(parentLot);
        g.setChildLotNumber(childLot);
        g.setProductId(productId);
        g.setTransformationType("SPLIT");
        return genealogyRepo.save(g);
    }

    /**
     * Performs the traceForward operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param lotNumber the lotNumber input value
     * @return List of matching records
     */
    @Transactional(readOnly = true)
    public List<LotGenealogy> traceForward(Long companyId, String lotNumber) {
        return genealogyRepo.findByCompanyIdAndParentLotNumber(companyId, lotNumber);
    }
}