package com.plus33.erp.iot.telemetry;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class MachineryTelemetryService {
    @Autowired PlatformMachineryTelemetryRepository telemetryRepo;

    @Transactional
    public PlatformMachineryTelemetry ingest(Long deviceId, Long signalId, BigDecimal val, String unit, long seq) {
        PlatformMachineryTelemetry t = new PlatformMachineryTelemetry();
        t.setDeviceId(deviceId);
        t.setSignalId(signalId);
        t.setRecordedAt(LocalDateTime.now());
        t.setQuality("GOOD");
        t.setValue(val);
        t.setUnit(unit);
        t.setSequenceNum(seq);
        return telemetryRepo.save(t);
    }
}