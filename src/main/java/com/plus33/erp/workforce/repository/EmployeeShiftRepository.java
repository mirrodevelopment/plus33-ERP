package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.EmployeeShift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeShiftRepository extends JpaRepository<EmployeeShift, EmployeeShift.EmployeeShiftId> {

    @Query("SELECT es FROM EmployeeShift es WHERE es.id.employeeId = :employeeId " +
           "AND es.id.effectiveFrom <= :date AND (es.effectiveTo IS NULL OR es.effectiveTo >= :date)")
    Optional<EmployeeShift> findActiveShiftForEmployeeOnDate(
            @Param("employeeId") Long employeeId,
            @Param("date") LocalDate date);

    @Query("SELECT es FROM EmployeeShift es WHERE es.id.employeeId IN :employeeIds " +
           "AND es.id.effectiveFrom <= :endDate AND (es.effectiveTo IS NULL OR es.effectiveTo >= :startDate)")
    List<EmployeeShift> findByEmployeeIdsInDateRange(
            @Param("employeeIds") List<Long> employeeIds,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT es FROM EmployeeShift es WHERE es.id.employeeId = :employeeId " +
           "AND es.id.effectiveFrom <= :endDate AND (es.effectiveTo IS NULL OR es.effectiveTo >= :startDate)")
    List<EmployeeShift> findOverlapping(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    void deleteByIdEmployeeIdAndIdShiftIdAndIdEffectiveFrom(Long employeeId, Long shiftId, LocalDate effectiveFrom);
}

