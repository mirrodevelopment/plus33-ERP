package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.Roster;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RosterRepository extends JpaRepository<Roster, Long> {
    List<Roster> findByEmployeeId(Long employeeId);
}
