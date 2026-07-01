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

@Service
public class SagaCoordinator {
    @Autowired IntegrationSagaRepository sagaRepo;
    @Autowired IntegrationSagaStepRepository stepRepo;

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
