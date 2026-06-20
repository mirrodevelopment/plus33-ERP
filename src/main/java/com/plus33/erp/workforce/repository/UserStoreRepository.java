package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.UserStore;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserStoreRepository extends JpaRepository<UserStore, UserStore.UserStoreId> {
    boolean existsByIdStoreId(Long storeId);
    List<UserStore> findByIdUserId(Long userId);
    void deleteByIdUserId(Long userId);
}
