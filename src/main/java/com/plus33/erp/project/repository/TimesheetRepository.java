package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {
    List<Timesheet> findByResourceId(Long resourceId);
}
