package com.plus33.erp.esm.repository;

import com.plus33.erp.esm.entity.TechnicianSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TechnicianSessionRepository extends JpaRepository<TechnicianSession, Long> {
    List<TechnicianSession> findByTechnicianIdAndActive(Long technicianId, Boolean active);
}
