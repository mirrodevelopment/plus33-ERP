package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.UserStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserStoreRepository extends JpaRepository<UserStore, UserStore.UserStoreId> {
    boolean existsByIdStoreId(Long storeId);
    List<UserStore> findByIdUserId(Long userId);
    List<UserStore> findByIdStoreId(Long storeId);
    void deleteByIdUserId(Long userId);

    @Query("SELECT COUNT(us) FROM UserStore us WHERE us.id.storeId = :storeId")
    long countByStoreId(@Param("storeId") Long storeId);
}
