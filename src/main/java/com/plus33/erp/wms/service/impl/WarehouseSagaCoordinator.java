/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.service.impl
 * File              : WarehouseSagaCoordinator.java
 * Purpose           : Business logic service layer for Wms Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseSagaCoordinatorController
 * Related Service   : WarehouseSagaCoordinator
 * Related Repository: WarehouseSagaStateRepository
 * Related Entity    : WarehouseSagaCoordinator
 * Related DTO       : N/A
 * Related Mapper    : WarehouseSagaCoordinatorMapper
 * Related DB Table  : warehouse_saga_coordinators
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseSagaCoordinatorController, WarehouseSagaCoordinatorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Wms Module. Implements WarehouseSagaCoordinatorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.wms.service.impl;

import com.plus33.erp.wms.entity.WarehouseSagaState;
import com.plus33.erp.wms.repository.WarehouseSagaStateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseSagaCoordinator}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.service.impl}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Wms Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * WarehouseSagaCoordinatorController
 *   --> WarehouseSagaCoordinator (this)
 *   --> Validate business rules
 *   --> WarehouseSagaCoordinatorRepository (read/write 'warehouse_saga_coordinators')
 *   --> WarehouseSagaCoordinatorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code warehouse_saga_coordinators}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional
public class WarehouseSagaCoordinator {

    private final WarehouseSagaStateRepository sagaRepo;

    public WarehouseSagaCoordinator(WarehouseSagaStateRepository sagaRepo) {
        this.sagaRepo = sagaRepo;
    }

    /**
     * Performs the startSaga operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param sagaType the sagaType input value
     * @param correlationId the correlationId input value
     * @param initialStep the initialStep input value
     * @param payloadJson the payloadJson input value
     * @return the WarehouseSagaState result
     */
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

    /**
     * Performs the transitionSaga operation in this module.
     *
     * @param correlationId the correlationId input value
     * @param nextStep the nextStep input value
     * @param status status filter for narrowing query results
     * @return the WarehouseSagaState result
     */
    public WarehouseSagaState transitionSaga(String correlationId, String nextStep, String status) {
        WarehouseSagaState saga = sagaRepo.findByCorrelationId(correlationId)
                .orElseThrow(() -> new IllegalArgumentException("Saga correlation not found: " + correlationId));
        saga.setCurrentStep(nextStep);
        saga.setStatus(status);
        return sagaRepo.save(saga);
    }
}