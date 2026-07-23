package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.EmployeeLeaveBalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeLeaveBalanceHistoryRepository extends JpaRepository<EmployeeLeaveBalanceHistory, Long> {

    /** All snapshots for an employee in a given year, ordered by snapshot date desc. */
    List<EmployeeLeaveBalanceHistory> findByEmployeeIdAndYearOrderBySnapshotDateDesc(Long employeeId, Integer year);

    /** All snapshots for an employee and leave type in a given year. */
    List<EmployeeLeaveBalanceHistory> findByEmployeeIdAndLeaveTypeIdAndYear(
            Long employeeId, Long leaveTypeId, Integer year);

    /** The most recent snapshot for an employee, leave type, and year (for carry-forward logic). */
    @Query("""
            SELECT h FROM EmployeeLeaveBalanceHistory h
            WHERE h.employee.id   = :employeeId
              AND h.leaveType.id  = :leaveTypeId
              AND h.year          = :year
            ORDER BY h.snapshotDate DESC
            """)
    List<EmployeeLeaveBalanceHistory> findLatestSnapshot(
            @Param("employeeId")   Long employeeId,
            @Param("leaveTypeId")  Long leaveTypeId,
            @Param("year")         Integer year);

    /** Returns the most recent snapshot, if any. */
    default Optional<EmployeeLeaveBalanceHistory> findLatestSnapshotOpt(Long employeeId, Long leaveTypeId, Integer year) {
        List<EmployeeLeaveBalanceHistory> results = findLatestSnapshot(employeeId, leaveTypeId, year);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    /** All snapshots for a full year across all employees (HR batch view). */
    List<EmployeeLeaveBalanceHistory> findByYear(Integer year);
}
