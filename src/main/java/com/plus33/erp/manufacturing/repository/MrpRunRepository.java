package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.MrpRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MrpRunRepository extends JpaRepository<MrpRun, Long> {

    Optional<MrpRun> findByCompanyIdAndRunNumber(Long companyId, String runNumber);

    List<MrpRun> findByCompanyIdOrderByCreatedAtDesc(Long companyId);

    @Query("""
        SELECT r FROM MrpRun r
        WHERE r.companyId = :companyId
          AND r.status = 'COMPLETED'
        ORDER BY r.completedAt DESC
    """)
    List<MrpRun> findCompletedRuns(@Param("companyId") Long companyId);
}
