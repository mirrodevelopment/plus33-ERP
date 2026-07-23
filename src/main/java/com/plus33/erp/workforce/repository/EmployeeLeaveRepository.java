package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.EmployeeLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeLeaveRepository extends JpaRepository<EmployeeLeave, Long> {

    List<EmployeeLeave> findByEmployeeIdOrderByCreatedAtDesc(Long employeeId);

    List<EmployeeLeave> findByStatus(String status);

    @Query("SELECT el FROM EmployeeLeave el WHERE el.status = 'PENDING' AND " +
           "(el.approverRole = :level OR " +
           "(el.approverRole IN ('SUPERVISOR', 'STORE_ADMIN') AND :level IN ('SUPERVISOR', 'STORE_ADMIN')))")
    List<EmployeeLeave> findPendingByApprovalLevel(@Param("level") String level);

    @Query("SELECT el FROM EmployeeLeave el " +
           "WHERE el.employee.id = :empId " +
           "AND el.status NOT IN ('CANCELLED', 'REJECTED') " +
           "AND el.startDate <= :endDate AND el.endDate >= :startDate")
    List<EmployeeLeave> findOverlapping(
            @Param("empId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(el) > 0 FROM EmployeeLeave el " +
           "WHERE el.employee.id = :empId " +
           "AND el.leaveType.code = :code " +
           "AND el.status = 'APPROVED'")
    boolean existsApprovedByEmployeeAndType(@Param("empId") Long employeeId, @Param("code") String code);

    @Query("SELECT COUNT(el) FROM EmployeeLeave el " +
           "WHERE el.employee.id = :empId " +
           "AND el.leaveType.id = :typeId " +
           "AND el.status = 'PENDING'")
    long countPendingByEmployeeAndType(@Param("empId") Long employeeId, @Param("typeId") Long leaveTypeId);

    @Query("SELECT el FROM EmployeeLeave el " +
           "WHERE el.cancellationRequested = TRUE AND el.status = 'APPROVED' AND " +
           "(el.approverRole = :level OR " +
           "(el.approverRole IN ('SUPERVISOR', 'STORE_ADMIN') AND :level IN ('SUPERVISOR', 'STORE_ADMIN')))")
    List<EmployeeLeave> findCancellationRequests(@Param("level") String level);

    @Query("SELECT el FROM EmployeeLeave el " +
           "WHERE el.employee.user.id IN (SELECT us.user.id FROM UserStore us WHERE us.store.id = :storeId) " +
           "AND el.status = 'APPROVED' " +
           "AND el.startDate <= :endDate AND el.endDate >= :startDate")
    List<EmployeeLeave> findApprovedByStoreAndDateRange(
            @Param("storeId") Long storeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
