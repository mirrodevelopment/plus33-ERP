package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.EmployeeSalaryStructureItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeSalaryStructureItemRepository extends JpaRepository<EmployeeSalaryStructureItem, Long> {
    List<EmployeeSalaryStructureItem> findByStructureId(Long structureId);
}
