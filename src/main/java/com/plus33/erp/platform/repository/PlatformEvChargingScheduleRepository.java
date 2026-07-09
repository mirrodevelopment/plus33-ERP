/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformEvChargingScheduleRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformEvChargingScheduleController
 * Related Service   : PlatformEvChargingScheduleService, PlatformEvChargingScheduleServiceImpl
 * Related Repository: PlatformEvChargingScheduleRepository
 * Related Entity    : PlatformEvChargingSchedule
 * Related DTO       : N/A
 * Related Mapper    : PlatformEvChargingScheduleMapper
 * Related DB Table  : platform_ev_charging_schedules
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformEvChargingScheduleService, PlatformEvChargingScheduleServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_ev_charging_schedules' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformEvChargingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformEvChargingScheduleRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_ev_charging_schedules' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_ev_charging_schedules}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformEvChargingScheduleRepository extends JpaRepository<PlatformEvChargingSchedule, Long> {
}