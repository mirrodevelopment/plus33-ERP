/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformScadaSignalRegisterRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformScadaSignalRegisterController
 * Related Service   : PlatformScadaSignalRegisterService, PlatformScadaSignalRegisterServiceImpl
 * Related Repository: PlatformScadaSignalRegisterRepository
 * Related Entity    : PlatformScadaSignalRegister
 * Related DTO       : N/A
 * Related Mapper    : PlatformScadaSignalRegisterMapper
 * Related DB Table  : platform_scada_signal_registers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformScadaSignalRegisterService, PlatformScadaSignalRegisterServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_scada_signal_registers' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformScadaSignalRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformScadaSignalRegisterRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_scada_signal_registers' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_scada_signal_registers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformScadaSignalRegisterRepository extends JpaRepository<PlatformScadaSignalRegister, Long> {
}