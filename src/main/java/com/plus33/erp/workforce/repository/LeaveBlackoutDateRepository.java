package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.LeaveBlackoutDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LeaveBlackoutDateRepository extends JpaRepository<LeaveBlackoutDate, Long> {

    @Query("SELECT b FROM LeaveBlackoutDate b " +
           "WHERE b.companyId = :companyId " +
           "AND b.active = TRUE " +
           "AND b.startDate <= :endDate AND b.endDate >= :startDate")
    List<LeaveBlackoutDate> findActiveOverlapping(
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<LeaveBlackoutDate> findByCompanyIdAndActiveTrue(Long companyId);
}
