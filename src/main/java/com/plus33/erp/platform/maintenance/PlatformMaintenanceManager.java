package com.plus33.erp.platform.maintenance;

import com.plus33.erp.platform.entity.PlatformMaintenanceWindow;
import com.plus33.erp.platform.repository.PlatformMaintenanceWindowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class PlatformMaintenanceManager {
    @Autowired PlatformMaintenanceWindowRepository windowRepo;

    @Transactional
    public PlatformMaintenanceWindow createScheduledWindow(LocalDateTime start, LocalDateTime end, String services, String msg, String allowedUsers) {
        PlatformMaintenanceWindow window = new PlatformMaintenanceWindow();
        window.setStartTime(start);
        window.setEndTime(end);
        window.setAffectedServices(services);
        window.setNotificationMsg(msg);
        window.setAllowedUsers(allowedUsers);
        window.setActive(true);
        return windowRepo.save(window);
    }

    public boolean isServiceUnderMaintenance(String serviceName, String username) {
        LocalDateTime now = LocalDateTime.now();
        return windowRepo.findAll().stream()
                .filter(PlatformMaintenanceWindow::getActive)
                .filter(w -> now.isAfter(w.getStartTime()) && now.isBefore(w.getEndTime()))
                .filter(w -> w.getAffectedServices().contains("*") || w.getAffectedServices().contains(serviceName))
                .anyMatch(w -> w.getAllowedUsers() == null || !w.getAllowedUsers().contains(username));
    }
}