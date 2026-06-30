package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MachineRepository extends JpaRepository<Machine, Long> {
    List<Machine> findByCompanyId(Long companyId);
    List<Machine> findByWorkCenterId(Long workCenterId);
    boolean existsByCompanyIdAndCode(Long companyId, String code);
}
