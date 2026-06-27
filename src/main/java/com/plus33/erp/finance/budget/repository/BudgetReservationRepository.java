package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.BudgetReservation;
import com.plus33.erp.finance.budget.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetReservationRepository extends JpaRepository<BudgetReservation, Long> {
    Optional<BudgetReservation> findBySourceModuleAndSourceReferenceId(String sourceModule, Long sourceReferenceId);
    List<BudgetReservation> findAllByBudgetLineId(Long budgetLineId);
    List<BudgetReservation> findAllByStatusAndExpiryDateBefore(ReservationStatus status, LocalDate date);
}
