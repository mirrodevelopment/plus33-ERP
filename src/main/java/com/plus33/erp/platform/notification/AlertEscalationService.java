package com.plus33.erp.platform.notification;

import com.plus33.erp.platform.entity.PlatformAnomalyAlert;
import com.plus33.erp.platform.repository.PlatformAnomalyAlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class AlertEscalationService {
    @Autowired PlatformAnomalyAlertRepository alertRepo;

    @Transactional
    public void triggerAlert(String name, String severity, String message) {
        PlatformAnomalyAlert alert = new PlatformAnomalyAlert();
        alert.setAlertName(name);
        alert.setSeverity(severity);
        alert.setStatus("ACTIVE");
        alert.setTriggerMessage(message);
        alert.setTimestamp(LocalDateTime.now());
        alertRepo.save(alert);
        
        // Simulating Escalation Level notifications L1 -> L2 -> L3
        System.out.println("[Alert Escalation Service] L1 notification triggered for alert: " + name);
    }
}