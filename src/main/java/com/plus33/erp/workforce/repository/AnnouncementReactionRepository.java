package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.AnnouncementReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnnouncementReactionRepository extends JpaRepository<AnnouncementReaction, Long> {
    List<AnnouncementReaction> findByAnnouncementId(Long announcementId);
    List<AnnouncementReaction> findByAnnouncementIdAndUsername(Long announcementId, String username);
}
