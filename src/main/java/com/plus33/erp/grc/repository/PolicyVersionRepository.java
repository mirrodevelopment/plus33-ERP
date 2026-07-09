/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.repository
 * File              : PolicyVersionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Grc Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PolicyVersionController
 * Related Service   : PolicyVersionService, PolicyVersionServiceImpl
 * Related Repository: PolicyVersionRepository
 * Related Entity    : PolicyVersion
 * Related DTO       : N/A
 * Related Mapper    : PolicyVersionMapper
 * Related DB Table  : policy_versions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PolicyVersionService, PolicyVersionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Grc Module against the 'policy_versions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface PolicyVersionRepository extends JpaRepository<PolicyVersion, Long> {
    List<PolicyVersion> findByPolicyId(Long policyId);
    Optional<PolicyVersion> findByPolicyIdAndVersionNumber(Long policyId, Integer versionNumber);
}
