package com.plus33.erp.iot.alarm;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AlarmOrchestrator {
    @Autowired PlatformScadaAlarmPolicyRepository policyRepo;
    @Autowired PlatformScadaAlarmEventRepository eventRepo;

    @Transactional
    public PlatformScadaAlarmPolicy createPolicy(String code, String severity, BigDecimal threshold) {
        PlatformScadaAlarmPolicy p = new PlatformScadaAlarmPolicy();
        p.setPolicyCode(code);
        p.setSeverity(severity);
        p.setThresholdValue(threshold);
        return policyRepo.save(p);
    }

    @Transactional
    public void triggerAlarm(Long deviceId, Long policyId, String msg) {
        PlatformScadaAlarmEvent event = new PlatformScadaAlarmEvent();
        event.setDeviceId(deviceId);
        event.setPolicyId(policyId);
        event.setAlarmStatus("ACTIVE");
        event.setMessage(msg);
        event.setTriggeredAt(LocalDateTime.now());
        eventRepo.save(event);
    }
}