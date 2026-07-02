package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformEsgScope1Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformEsgScope1LogRepository extends JpaRepository<PlatformEsgScope1Log, Long> {
}