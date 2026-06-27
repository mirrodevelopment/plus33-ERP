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
