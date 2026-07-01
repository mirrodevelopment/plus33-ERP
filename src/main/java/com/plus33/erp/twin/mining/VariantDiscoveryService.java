package com.plus33.erp.twin.mining;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VariantDiscoveryService {
    @Autowired PlatformProcessEventLogRepository eventLogRepo;

    public List<PlatformProcessEventLog> discoverVariant(Long caseId) {
        return eventLogRepo.findAll().stream()
                .filter(e -> e.getCaseId().equals(caseId))
                .toList();
    }
}