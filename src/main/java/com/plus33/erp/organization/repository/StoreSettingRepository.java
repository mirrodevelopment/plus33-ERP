package com.plus33.erp.organization.repository;

import com.plus33.erp.organization.entity.StoreSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * JPA Repository providing database CRUD operations for StoreSetting entity against 'store_settings' table.
 */
public interface StoreSettingRepository extends JpaRepository<StoreSetting, Long> {
    Optional<StoreSetting> findByStoreId(Long storeId);
}
