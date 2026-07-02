package com.plus33.erp.iot.scada;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScadaDeviceManager {
    @Autowired PlatformScadaDeviceRepository deviceRepo;
    @Autowired PlatformScadaSignalRegisterRepository registerRepo;

    @Transactional
    public PlatformScadaDevice registerDevice(String code, String type, String ns, String nodeId, String plc, int unitId) {
        PlatformScadaDevice d = new PlatformScadaDevice();
        d.setDeviceCode(code);
        d.setDeviceType(type);
        d.setOpcUaNamespace(ns);
        d.setNodeId(nodeId);
        d.setPlcAddress(plc);
        d.setModbusUnitId(unitId);
        return deviceRepo.save(d);
    }

    @Transactional
    public PlatformScadaSignalRegister registerSignal(Long deviceId, String code, String type) {
        PlatformScadaSignalRegister r = new PlatformScadaSignalRegister();
        r.setDeviceId(deviceId);
        r.setRegisterCode(code);
        r.setRegisterType(type);
        return registerRepo.save(r);
    }
}