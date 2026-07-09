/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.repository
 * File              : BudgetLineRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetLineController
 * Related Service   : BudgetLineService, BudgetLineServiceImpl
 * Related Repository: BudgetLineRepository
 * Related Entity    : BudgetLine
 * Related DTO       : N/A
 * Related Mapper    : BudgetLineMapper
 * Related DB Table  : budget_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetLineService, BudgetLineServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'budget_lines' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.BudgetLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetLineRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'budget_lines' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_lines}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BudgetLineRepository extends JpaRepository<BudgetLine, Long>, JpaSpecificationExecutor<BudgetLine> {

    List<BudgetLine> findAllByBudgetId(Long budgetId);
    List<BudgetLine> findAllByBudgetVersionId(Long budgetVersionId);

    @Query("SELECT bl FROM BudgetLine bl WHERE bl.budget.id = :budgetId " +
           "AND bl.budgetVersion.id = :versionId " +
           "AND bl.account.id = :accountId " +
           "AND bl.dimensionSet.id = :dimSetId " +
           "AND bl.periodStartDate <= :txDate " +
           "AND bl.periodEndDate >= :txDate")
    Optional<BudgetLine> findMatchingLine(
        @Param("budgetId") Long budgetId,
        @Param("versionId") Long versionId,
        @Param("accountId") Long accountId,
        @Param("dimSetId") Long dimSetId,
        @Param("txDate") LocalDate txDate
    );

    @Query("SELECT bl FROM BudgetLine bl WHERE bl.budget.company.id = :companyId " +
           "AND bl.budget.fiscalYear.id = :fiscalYearId " +
           "AND bl.budget.isActive = true " +
           "AND bl.budget.status = 'APPROVED' " +
           "AND bl.account.id = :accountId " +
           "AND bl.dimensionSet.id = :dimSetId " +
           "AND bl.periodStartDate <= :txDate " +
           "AND bl.periodEndDate >= :txDate")
    List<BudgetLine> findActiveBudgetLines(
        @Param("companyId") Long companyId,
        @Param("fiscalYearId") Long fiscalYearId,
        @Param("accountId") Long accountId,
        @Param("dimSetId") Long dimSetId,
        @Param("txDate") LocalDate txDate
    );
}