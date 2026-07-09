/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.repository
 * File              : PolicyAcknowledgementRepository.java
 * Purpose           : JPA Repository providing database CRUD for Grc Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PolicyAcknowledgementController
 * Related Service   : PolicyAcknowledgementService, PolicyAcknowledgementServiceImpl
 * Related Repository: PolicyAcknowledgementRepository
 * Related Entity    : PolicyAcknowledgement
 * Related DTO       : N/A
 * Related Mapper    : PolicyAcknowledgementMapper
 * Related DB Table  : policy_acknowledgements
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PolicyAcknowledgementService, PolicyAcknowledgementServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Grc Module against the 'policy_acknowledgements' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PolicyAcknowledgementRepository extends JpaRepository<PolicyAcknowledgement, Long> {
    List<PolicyAcknowledgement> findByPolicyVersionId(Long policyVersionId);
    boolean existsByPolicyVersionIdAndEmployeeId(Long policyVersionId, Long employeeId);
    long countByPolicyVersionId(Long policyVersionId);
}
