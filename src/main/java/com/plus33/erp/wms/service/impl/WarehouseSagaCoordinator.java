package com.plus33.erp.wms.service.impl;

import com.plus33.erp.wms.entity.WarehouseSagaState;
import com.plus33.erp.wms.repository.WarehouseSagaStateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WarehouseSagaCoordinator {

    private final WarehouseSagaStateRepository sagaRepo;

    public WarehouseSagaCoordinator(WarehouseSagaStateRepository sagaRepo) {
        this.sagaRepo = sagaRepo;
    }

    public WarehouseSagaState startSaga(Long companyId, String sagaType, String correlationId, String initialStep, String payloadJson) {
        WarehouseSagaState saga = new WarehouseSagaState();
        saga.setCompanyId(companyId);
        saga.setSagaType(sagaType);
        saga.setCorrelationId(correlationId);
        saga.setCurrentStep(initialStep);
        saga.setPayloadJson(payloadJson);
        saga.setStatus("IN_PROGRESS");
        return sagaRepo.save(saga);
    }

    public WarehouseSagaState transitionSaga(String correlationId, String nextStep, String status) {
        WarehouseSagaState saga = sagaRepo.findByCorrelationId(correlationId)
                .orElseThrow(() -> new IllegalArgumentException("Saga correlation not found: " + correlationId));
        saga.setCurrentStep(nextStep);
        saga.setStatus(status);
        return sagaRepo.save(saga);
    }
}
