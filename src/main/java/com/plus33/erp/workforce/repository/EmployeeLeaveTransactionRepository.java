package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.EmployeeLeaveTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface EmployeeLeaveTransactionRepository extends JpaRepository<EmployeeLeaveTransaction, Long> {

    List<EmployeeLeaveTransaction> findByEmployeeIdAndLeaveTypeIdOrderByCreatedAtDesc(
            Long employeeId, Long leaveTypeId);

    @Query("SELECT COALESCE(SUM(t.days), 0) FROM EmployeeLeaveTransaction t " +
           "WHERE t.employee.id = :empId AND t.leaveType.id = :typeId " +
           "AND YEAR(t.createdAt) = :year")
    BigDecimal sumDaysByEmployeeTypeAndYear(
            @Param("empId") Long employeeId,
            @Param("typeId") Long leaveTypeId,
            @Param("year") int year);

    @Query("SELECT COUNT(t) > 0 FROM EmployeeLeaveTransaction t " +
           "WHERE t.employee.id = :empId AND t.transactionType = :txnType AND t.note = :note")
    boolean existsByEmployeeIdAndTransactionTypeAndNote(
            @Param("empId") Long employeeId,
            @Param("txnType") String transactionType,
            @Param("note") String note);
}
