package com.plus33.erp.esm.service;

import com.plus33.erp.esm.entity.EsmWorkOrder;
import com.plus33.erp.esm.entity.IotDeviceAlarm;
import com.plus33.erp.esm.repository.EsmWorkOrderRepository;
import com.plus33.erp.esm.repository.IotDeviceAlarmRepository;
import com.plus33.erp.esm.event.EsmEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class IotAlarmProcessor {

    private final IotDeviceAlarmRepository alarmRepository;
    private final EsmWorkOrderRepository workOrderRepository;
    private final EsmEventBus eventBus;

    public IotAlarmProcessor(IotDeviceAlarmRepository alarmRepository,
                             EsmWorkOrderRepository workOrderRepository,
                             EsmEventBus eventBus) {
        this.alarmRepository = alarmRepository;
        this.workOrderRepository = workOrderRepository;
        this.eventBus = eventBus;
    }

    @Transactional
    public void receiveAlarm(Long companyId, String deviceId, String alarmType, BigDecimal metricValue, String severity) {
        // Duplicate suppression: check if an unprocessed alarm of the same type for this device already exists
        List<IotDeviceAlarm> existingUnprocessed = alarmRepository.findByProcessed(false);
        boolean duplicate = existingUnprocessed.stream()
                .anyMatch(a -> a.getDeviceId().equals(deviceId) && a.getAlarmType().equals(alarmType));

        if (duplicate) {
            return; // Suppressed
        }

        IotDeviceAlarm alarm = new IotDeviceAlarm();
        alarm.setCompanyId(companyId);
        alarm.setDeviceId(deviceId);
        alarm.setAlarmType(alarmType);
        alarm.setMetricValue(metricValue);
        alarm.setSeverity(severity);
        alarm.setProcessed(false);
        alarmRepository.save(alarm);

        if ("CRITICAL".equals(severity)) {
            // Auto trigger work order
            EsmWorkOrder wo = new EsmWorkOrder();
            wo.setCompanyId(companyId);
            wo.setCustomerId(1L);
            wo.setWorkOrderNumber("WO-IOT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            wo.setStatus("DRAFT");
            wo.setPriority("URGENT");
            wo.setScheduledAt(LocalDateTime.now());
            workOrderRepository.save(wo);

            alarm.setProcessed(true);
            alarmRepository.save(alarm);

            eventBus.publish("WorkOrderCreated", companyId, wo.getId(), "IoT critical alarm work order created");
        }
    }
}
