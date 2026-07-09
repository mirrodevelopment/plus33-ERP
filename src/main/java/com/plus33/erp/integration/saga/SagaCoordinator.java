/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.saga
 * File              : SagaCoordinator.java
 * Purpose           : Business logic service layer for Integration Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SagaCoordinatorController
 * Related Service   : SagaCoordinator
 * Related Repository: SagaCoordinatorRepository
 * Related Entity    : SagaCoordinator
 * Related DTO       : N/A
 * Related Mapper    : SagaCoordinatorMapper
 * Related DB Table  : saga_coordinators
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SagaCoordinatorController, SagaCoordinatorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Integration Module. Implements SagaCoordinatorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.integration.saga;

import com.plus33.erp.integration.entity.IntegrationSaga;
import com.plus33.erp.integration.entity.IntegrationSagaStep;
import com.plus33.erp.integration.repository.IntegrationSagaRepository;
import com.plus33.erp.integration.repository.IntegrationSagaStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code SagaCoordinator}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.saga}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Integration Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * SagaCoordinatorController
 *   --> SagaCoordinator (this)
 *   --> Validate business rules
 *   --> SagaCoordinatorRepository (read/write 'saga_coordinators')
 *   --> SagaCoordinatorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code saga_coordinators}</p>
 * <p><b>Module Deps      :</b> Integration</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class SagaCoordinator {
    @Autowired IntegrationSagaRepository sagaRepo;
    @Autowired IntegrationSagaStepRepository stepRepo;
    /**
     * Performs the executeSaga operation in this module.
     *
     * @param sagaType the sagaType input value
     * @param payloadJson the payloadJson input value
     * @param steps the steps input value
     * @return the numeric result value
     */
    @Transactional
    public IntegrationSaga executeSaga(String sagaType, String payloadJson, List<IntegrationSagaStep> steps) {
        IntegrationSaga saga = new IntegrationSaga();
        saga.setSagaCode("SAGA-" + System.currentTimeMillis() + "-" + java.util.UUID.randomUUID().toString().substring(0, 8));
        saga.setSagaType(sagaType);
        saga.setStatus("RUNNING");
        saga.setPayloadJson(payloadJson);
        saga.setCreatedAt(LocalDateTime.now());
        saga.setUpdatedAt(LocalDateTime.now());
        sagaRepo.save(saga);

        boolean hasFailure = false;

        for (IntegrationSagaStep step : steps) {
            step.setSagaCode(saga.getSagaCode());
            stepRepo.save(step);

            if ("FAILED".equals(step.getStatus())) {
                hasFailure = true;
                break;
            }
        }

        if (hasFailure) {
            saga.setStatus("COMPENSATING");

            List<IntegrationSagaStep> activeSteps = stepRepo.findAll().stream()
                    .filter(s -> s.getSagaCode().equals(saga.getSagaCode()))
                    .sorted(Comparator.comparingInt(IntegrationSagaStep::getExecutionOrder).reversed())
                    .toList();

            for (IntegrationSagaStep activeStep : activeSteps) {
                if ("COMPLETED".equals(activeStep.getStatus())) {
                    activeStep.setStatus("COMPENSATED");
                    stepRepo.save(activeStep);
                }
            }
            saga.setStatus("FAILED");
        } else {
            saga.setStatus("COMPLETED");
        }
        
        saga.setUpdatedAt(LocalDateTime.now());
        return saga;
    }
}