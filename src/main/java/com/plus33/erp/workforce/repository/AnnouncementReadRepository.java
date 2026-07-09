package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.AnnouncementRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnnouncementReadRepository extends JpaRepository<AnnouncementRead, Long> {
    List<AnnouncementRead> findByUsername(String username);
    Optional<AnnouncementRead> findByAnnouncementIdAndUsername(Long announcementId, String username);
}
