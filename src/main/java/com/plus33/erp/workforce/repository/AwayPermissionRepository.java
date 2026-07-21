package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.AwayPermissionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p>JPA Repository for {@link AwayPermissionRequest} against table {@code away_permission_requests}.</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface AwayPermissionRepository extends JpaRepository<AwayPermissionRequest, Long> {

    /** All pending requests for a store (used by supervisor/admin dashboard). */
    List<AwayPermissionRequest> findByStoreIdAndStatusOrderByRequestedAtAsc(Long storeId, String status);

    /** Pending + extension-requested requests for a store. */
    @Query("SELECT r FROM AwayPermissionRequest r WHERE r.store.id = :storeId AND r.status IN ('PENDING','EXTENSION_REQUESTED') ORDER BY r.requestedAt ASC")
    List<AwayPermissionRequest> findPendingForStore(@Param("storeId") Long storeId);

    /** Ongoing (currently approved) requests for a store. */
    @Query("SELECT r FROM AwayPermissionRequest r WHERE r.store.id = :storeId AND r.status = 'APPROVED' ORDER BY r.approvedUntil ASC")
    List<AwayPermissionRequest> findOngoingForStore(@Param("storeId") Long storeId);

    /** Active approved pass for a given attendance record (used during GPS ping). */
    @Query("SELECT r FROM AwayPermissionRequest r WHERE r.attendance.id = :attendanceId AND r.status IN ('APPROVED','EXTENSION_REQUESTED') AND r.approvedUntil IS NOT NULL ORDER BY r.approvedUntil DESC")
    List<AwayPermissionRequest> findActivePassesForAttendance(@Param("attendanceId") Long attendanceId);

    /** Latest request for an employee by attendance. */
    Optional<AwayPermissionRequest> findTopByAttendanceIdOrderByRequestedAtDesc(Long attendanceId);

    /** All requests by an employee for a specific attendance. */
    List<AwayPermissionRequest> findByAttendanceIdOrderByRequestedAtAsc(Long attendanceId);

    /** Check if employee already has a PENDING request for this attendance. */
    boolean existsByAttendanceIdAndStatus(Long attendanceId, String status);
}
