package com.plus33.erp.esm.repository;

import com.plus33.erp.esm.entity.IotDeviceAlarm;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IotDeviceAlarmRepository extends JpaRepository<IotDeviceAlarm, Long> {
    List<IotDeviceAlarm> findByProcessed(Boolean processed);
}
