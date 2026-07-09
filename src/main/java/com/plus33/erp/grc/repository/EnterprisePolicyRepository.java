/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.repository
 * File              : EnterprisePolicyRepository.java
 * Purpose           : JPA Repository providing database CRUD for Grc Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EnterprisePolicyController
 * Related Service   : EnterprisePolicyService, EnterprisePolicyServiceImpl
 * Related Repository: EnterprisePolicyRepository
 * Related Entity    : EnterprisePolicy
 * Related DTO       : N/A
 * Related Mapper    : EnterprisePolicyMapper
 * Related DB Table  : enterprise_policys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EnterprisePolicyService, EnterprisePolicyServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Grc Module against the 'enterprise_policys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface EnterprisePolicyRepository extends JpaRepository<EnterprisePolicy, Long> {
    Optional<EnterprisePolicy> findByPolicyCode(String code);
    List<EnterprisePolicy> findByCompanyIdAndStatus(Long companyId, String status);
}
