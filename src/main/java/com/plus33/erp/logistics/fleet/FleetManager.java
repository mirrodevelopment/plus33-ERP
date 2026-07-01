package com.plus33.erp.logistics.fleet;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class FleetManager {
    @Autowired PlatformVehicleRepository vehicleRepo;

    @Transactional
    public PlatformVehicle registerVehicle(String code, BigDecimal capacity) {
        PlatformVehicle v = new PlatformVehicle();
        v.setVehicleCode(code);
        v.setCapacityKg(capacity);
        v.setStatus("AVAILABLE");
        return vehicleRepo.save(v);
    }
}