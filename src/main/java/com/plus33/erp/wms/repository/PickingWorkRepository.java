package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.PickingWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PickingWorkRepository extends JpaRepository<PickingWork, Long> {
    List<PickingWork> findByWave_IdAndStatus(Long waveId, String status);
    List<PickingWork> findByCompanyIdAndAssignedToAndStatus(Long companyId, Long userId, String status);

    @Query("SELECT pw FROM PickingWork pw WHERE pw.wave.id = :waveId AND pw.status IN ('PENDING', 'ASSIGNED', 'IN_PROGRESS', 'PARTIALLY_PICKED')")
    List<PickingWork> findOpenByWave(@Param("waveId") Long waveId);
}
