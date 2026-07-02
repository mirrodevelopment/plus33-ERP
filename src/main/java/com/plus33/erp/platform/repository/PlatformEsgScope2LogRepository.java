package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformEsgScope2Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformEsgScope2LogRepository extends JpaRepository<PlatformEsgScope2Log, Long> {
}