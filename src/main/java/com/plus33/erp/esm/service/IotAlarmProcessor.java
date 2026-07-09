/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.service
 * File              : IotAlarmProcessor.java
 * Purpose           : Business logic service layer for Esm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IotAlarmProcessorController
 * Related Service   : IotAlarmProcessor
 * Related Repository: IotDeviceAlarmRepository, EsmWorkOrderRepository
 * Related Entity    : IotAlarmProcessor
 * Related DTO       : N/A
 * Related Mapper    : IotAlarmProcessorMapper
 * Related DB Table  : iot_alarm_processors
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IotAlarmProcessorController, IotAlarmProcessorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Esm Module. Implements IotAlarmProcessorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code IotAlarmProcessor}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Esm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * IotAlarmProcessorController
 *   --> IotAlarmProcessor (this)
 *   --> Validate business rules
 *   --> IotAlarmProcessorRepository (read/write 'iot_alarm_processors')
 *   --> IotAlarmProcessorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code iot_alarm_processors}</p>
 * <p><b>Module Deps      :</b> Esm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Performs the receiveAlarm operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param deviceId the deviceId input value
     * @param alarmType the alarmType input value
     * @param metricValue the metricValue input value
     * @param severity the severity input value
     */
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