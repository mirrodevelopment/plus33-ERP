package com.plus33.erp.wms.service.impl;

import com.plus33.erp.wms.entity.LotGenealogy;
import com.plus33.erp.wms.repository.LotGenealogyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LotSerialGenealogyService {

    private final LotGenealogyRepository genealogyRepo;

    public LotSerialGenealogyService(LotGenealogyRepository genealogyRepo) {
        this.genealogyRepo = genealogyRepo;
    }

    public LotGenealogy recordLotSplit(Long companyId, String parentLot, String childLot, Long productId) {
        LotGenealogy g = new LotGenealogy();
        g.setCompanyId(companyId);
        g.setParentLotNumber(parentLot);
        g.setChildLotNumber(childLot);
        g.setProductId(productId);
        g.setTransformationType("SPLIT");
        return genealogyRepo.save(g);
    }

    @Transactional(readOnly = true)
    public List<LotGenealogy> traceForward(Long companyId, String lotNumber) {
        return genealogyRepo.findByCompanyIdAndParentLotNumber(companyId, lotNumber);
    }
}
