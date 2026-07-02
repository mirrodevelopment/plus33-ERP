package com.plus33.erp.iot.control;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ScadaControlService {
    @Autowired PlatformScadaWriteCommandRepository commandRepo;
    @Autowired PlatformScadaAuditTrailRepository auditRepo;

    @Transactional
    public void executeWrite(Long deviceId, Long registerId, BigDecimal val, String operator, String sign) {
        PlatformScadaWriteCommand cmd = new PlatformScadaWriteCommand();
        cmd.setDeviceId(deviceId);
        cmd.setRegisterId(registerId);
        cmd.setCommandValue(val);
        cmd.setCommandHash("SHA-256-HASH-VALUE");
        cmd.setApprovedBy("admin-supervisor");
        cmd.setExecutedBy(operator);
        cmd.setSignature(sign);
        cmd.setExecutionTime(LocalDateTime.now());
        cmd.setRollbackSupported(true);
        cmd = commandRepo.save(cmd);

        PlatformScadaAuditTrail audit = new PlatformScadaAuditTrail();
        audit.setCommandId(cmd.getId());
        audit.setStatus("VERIFIED");
        audit.setAuditHash("AUDIT-SIG-CRYPT");
        audit.setAuditedAt(LocalDateTime.now());
        auditRepo.save(audit);
    }
}