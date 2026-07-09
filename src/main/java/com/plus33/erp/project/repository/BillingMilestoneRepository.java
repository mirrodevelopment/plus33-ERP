/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.repository
 * File              : BillingMilestoneRepository.java
 * Purpose           : JPA Repository providing database CRUD for Project Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BillingMilestoneController
 * Related Service   : BillingMilestoneService, BillingMilestoneServiceImpl
 * Related Repository: BillingMilestoneRepository
 * Related Entity    : BillingMilestone
 * Related DTO       : N/A
 * Related Mapper    : BillingMilestoneMapper
 * Related DB Table  : billing_milestones
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BillingMilestoneService, BillingMilestoneServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Project Module against the 'billing_milestones' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.BillingMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code BillingMilestoneRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'billing_milestones' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code billing_milestones}</p>
 * <p><b>Module Deps      :</b> Project</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface BillingMilestoneRepository extends JpaRepository<BillingMilestone, Long> {
    List<BillingMilestone> findByContractId(Long contractId);
}
