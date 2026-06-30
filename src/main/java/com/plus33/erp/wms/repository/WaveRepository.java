package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.Wave;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WaveRepository extends JpaRepository<Wave, Long> {
    Optional<Wave> findByCompanyIdAndWaveNumber(Long companyId, String waveNumber);
    List<Wave> findByCompanyIdAndWarehouseIdAndStatus(Long companyId, Long warehouseId, String status);
    List<Wave> findByCompanyIdAndStatus(Long companyId, String status);
}
