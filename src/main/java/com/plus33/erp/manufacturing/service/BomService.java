/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.service
 * File              : BomService.java
 * Purpose           : Service interface contract defining the API for Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BomController
 * Related Service   : BomService, BomServiceImpl
 * Related Repository: BomRepository
 * Related Entity    : Bom
 * Related DTO       : BomHeaderDto, CreateBomLineRequest, CreateBomRequest
 * Related Mapper    : BomMapper
 * Related DB Table  : boms
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Manufacturing Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Manufacturing Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.manufacturing.service;

import com.plus33.erp.manufacturing.dto.*;
import java.util.List;

public interface BomService {

    BomHeaderDto createBom(CreateBomRequest request);

    BomHeaderDto getBomById(Long id);

    List<BomHeaderDto> getBomsByCompany(Long companyId);

    BomHeaderDto approveBom(Long id, String reviewer);

    BomHeaderDto addBomLine(Long bomHeaderId, CreateBomLineRequest request);

    BomHeaderDto getActiveBomForProduct(Long companyId, Long productId);

    void deleteBom(Long companyId, Long bomId);
}
