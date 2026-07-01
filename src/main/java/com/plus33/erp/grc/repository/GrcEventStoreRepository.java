package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface GrcEventStoreRepository extends JpaRepository<GrcEventStoreItem, Long> {
    Optional<GrcEventStoreItem> findByIdempotencyKey(String key);
    List<GrcEventStoreItem> findByCompanyIdAndEventType(Long companyId, String eventType);
    long countByEventType(String eventType);
}
