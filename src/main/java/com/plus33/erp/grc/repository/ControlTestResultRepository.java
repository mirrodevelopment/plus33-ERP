/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.repository
 * File              : ControlTestResultRepository.java
 * Purpose           : JPA Repository providing database CRUD for Grc Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ControlTestResultController
 * Related Service   : ControlTestResultService, ControlTestResultServiceImpl
 * Related Repository: ControlTestResultRepository
 * Related Entity    : ControlTestResult
 * Related DTO       : N/A
 * Related Mapper    : ControlTestResultMapper
 * Related DB Table  : control_test_results
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ControlTestResultService, ControlTestResultServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Grc Module against the 'control_test_results' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ControlTestResultRepository extends JpaRepository<ControlTestResult, Long> {
    List<ControlTestResult> findByTestPlanId(Long testPlanId);
    long countByResult(String result);
}
