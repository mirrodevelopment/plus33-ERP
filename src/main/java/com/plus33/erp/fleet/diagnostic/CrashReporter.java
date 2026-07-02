package com.plus33.erp.fleet.diagnostic;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class CrashReporter {
    @Autowired PlatformDeviceDiagnosticRepository diagnosticRepo;

    @Transactional
    public PlatformDeviceDiagnostic reportCrash(Long nodeId, String exception, String stack) {
        PlatformDeviceDiagnostic d = new PlatformDeviceDiagnostic();
        d.setNodeId(nodeId);
        d.setExceptionMessage(exception);
        d.setStackTrace(stack);
        d.setThreadDump("Thread [main] (Suspended) \n\t at java.lang.Object.wait(Native Method)");
        d.setCoreDumpLocation("/var/log/crashed-dumps/core." + nodeId);
        d.setReportedAt(LocalDateTime.now());
        return diagnosticRepo.save(d);
    }
}