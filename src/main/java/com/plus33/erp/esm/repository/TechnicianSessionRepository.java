/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.repository
 * File              : TechnicianSessionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Esm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TechnicianSessionController
 * Related Service   : TechnicianSessionService, TechnicianSessionServiceImpl
 * Related Repository: TechnicianSessionRepository
 * Related Entity    : TechnicianSession
 * Related DTO       : N/A
 * Related Mapper    : TechnicianSessionMapper
 * Related DB Table  : technician_sessions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TechnicianSessionService, TechnicianSessionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Esm Module against the 'technician_sessions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.esm.repository;

import com.plus33.erp.esm.entity.TechnicianSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code TechnicianSessionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'technician_sessions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code technician_sessions}</p>
 * <p><b>Module Deps      :</b> Esm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface TechnicianSessionRepository extends JpaRepository<TechnicianSession, Long> {
    List<TechnicianSession> findByTechnicianIdAndActive(Long technicianId, Boolean active);
}
