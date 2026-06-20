package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.UserRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRegionRepository extends JpaRepository<UserRegion, UserRegion.UserRegionId> {
    boolean existsByIdRegionId(Long regionId);
    List<UserRegion> findByIdUserId(Long userId);
    void deleteByIdUserId(Long userId);
}
