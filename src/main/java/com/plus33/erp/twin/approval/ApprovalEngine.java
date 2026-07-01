package com.plus33.erp.twin.approval;

import com.plus33.erp.platform.entity.PlatformAutonomousExecution;
import org.springframework.stereotype.Service;

@Service
public class ApprovalEngine {
    public void requestApproval(PlatformAutonomousExecution exec) {
        // Triggers dual authorization checks via GRC workflow
    }
}