package com.plus33.erp.twin.mining;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerformanceAnalyzer {
    @Autowired PlatformProcessEventLogRepository eventLogRepo;

    public long analyzeAverageDuration(String activity) {
        return (long) eventLogRepo.findAll().stream()
                .filter(e -> e.getActivityName().equals(activity))
                .mapToLong(PlatformProcessEventLog::getDurationMs)
                .average()
                .orElse(0.0);
    }
}