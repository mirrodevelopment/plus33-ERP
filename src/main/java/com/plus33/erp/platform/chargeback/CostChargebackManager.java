package com.plus33.erp.platform.chargeback;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CostChargebackManager {
    @Autowired PlatformCostCenterRepository centerRepo;
    @Autowired PlatformCostAllocationRepository allocationRepo;
    @Autowired PlatformChargebackRepository chargebackRepo;

    @Transactional
    public void createCostCenter(String code, String name) {
        PlatformCostCenter cc = new PlatformCostCenter();
        cc.setCenterCode(code);
        cc.setCenterName(name);
        centerRepo.save(cc);
    }

    @Transactional
    public void allocateCost(String centerCode, String resource, double ratio) {
        PlatformCostCenter cc = centerRepo.findAll().stream()
                .filter(c -> c.getCenterCode().equals(centerCode))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Cost center not found"));

        PlatformCostAllocation alloc = new PlatformCostAllocation();
        alloc.setCostCenterId(cc.getId());
        alloc.setResourceId(resource);
        alloc.setAllocationRatio(BigDecimal.valueOf(ratio));
        allocationRepo.save(alloc);
    }

    @Transactional
    public void recordChargeback(String centerCode, double amount, String month) {
        PlatformCostCenter cc = centerRepo.findAll().stream()
                .filter(c -> c.getCenterCode().equals(centerCode))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Cost center not found"));

        PlatformChargeback cb = new PlatformChargeback();
        cb.setCostCenterId(cc.getId());
        cb.setAmount(BigDecimal.valueOf(amount));
        cb.setBillingMonth(month);
        cb.setRecordedAt(LocalDateTime.now());
        chargebackRepo.save(cb);
    }
}