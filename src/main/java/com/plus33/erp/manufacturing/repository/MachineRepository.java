/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : MachineRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MachineController
 * Related Service   : MachineService, MachineServiceImpl
 * Related Repository: MachineRepository
 * Related Entity    : Machine
 * Related DTO       : N/A
 * Related Mapper    : MachineMapper
 * Related DB Table  : machines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MachineService, MachineServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'machines' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code MachineRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'machines' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code machines}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface MachineRepository extends JpaRepository<Machine, Long> {
    List<Machine> findByCompanyId(Long companyId);
    List<Machine> findByWorkCenterId(Long workCenterId);
    boolean existsByCompanyIdAndCode(Long companyId, String code);
}
