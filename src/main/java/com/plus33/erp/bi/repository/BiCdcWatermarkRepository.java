package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiCdcWatermark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiCdcWatermarkRepository extends JpaRepository<BiCdcWatermark, Long> {
    java.util.Optional<BiCdcWatermark> findBySourceModuleAndSourceTable(String module, String table);
}
