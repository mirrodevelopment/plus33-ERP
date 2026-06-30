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
