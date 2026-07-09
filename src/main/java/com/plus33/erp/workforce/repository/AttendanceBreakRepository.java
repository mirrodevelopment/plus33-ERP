package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.AttendanceBreak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttendanceBreakRepository extends JpaRepository<AttendanceBreak, Long> {

    List<AttendanceBreak> findByAttendanceId(Long attendanceId);

    @Query("SELECT ab FROM AttendanceBreak ab WHERE ab.attendance.id = :attendanceId AND ab.breakEnd IS NULL")
    Optional<AttendanceBreak> findActiveBreakByAttendanceId(@Param("attendanceId") Long attendanceId);
}
