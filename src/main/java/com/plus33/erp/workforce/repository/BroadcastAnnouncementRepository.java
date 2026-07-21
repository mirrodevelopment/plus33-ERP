package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.BroadcastAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BroadcastAnnouncementRepository extends JpaRepository<BroadcastAnnouncement, Long> {

    /** Active announcements visible in standard user feeds (not deleted & not expired) */
    List<BroadcastAnnouncement> findByIsDeletedFalseAndExpiresAtAfterOrderByCreatedAtDesc(LocalDateTime now);

    /** All announcements including expired and soft-deleted for Ultimate Admin audit inspection */
    List<BroadcastAnnouncement> findAllByOrderByCreatedAtDesc();

    /** Hard purge query for soft-deleted records older than 60 days */
    @Modifying
    @Query("DELETE FROM BroadcastAnnouncement b WHERE b.isDeleted = true AND b.deletedAt < :purgeThreshold")
    int purgeExpiredSoftDeleted(@Param("purgeThreshold") LocalDateTime purgeThreshold);
}
