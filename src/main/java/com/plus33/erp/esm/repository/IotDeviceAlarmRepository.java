/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.repository
 * File              : IotDeviceAlarmRepository.java
 * Purpose           : JPA Repository providing database CRUD for Esm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IotDeviceAlarmController
 * Related Service   : IotDeviceAlarmService, IotDeviceAlarmServiceImpl
 * Related Repository: IotDeviceAlarmRepository
 * Related Entity    : IotDeviceAlarm
 * Related DTO       : N/A
 * Related Mapper    : IotDeviceAlarmMapper
 * Related DB Table  : iot_device_alarms
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IotDeviceAlarmService, IotDeviceAlarmServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Esm Module against the 'iot_device_alarms' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.esm.repository;

import com.plus33.erp.esm.entity.IotDeviceAlarm;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code IotDeviceAlarmRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'iot_device_alarms' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code iot_device_alarms}</p>
 * <p><b>Module Deps      :</b> Esm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface IotDeviceAlarmRepository extends JpaRepository<IotDeviceAlarm, Long> {
    List<IotDeviceAlarm> findByProcessed(Boolean processed);
}
