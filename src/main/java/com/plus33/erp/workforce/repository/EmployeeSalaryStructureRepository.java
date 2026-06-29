package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.EmployeeSalaryStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface EmployeeSalaryStructureRepository extends JpaRepository<EmployeeSalaryStructure, Long> {
    List<EmployeeSalaryStructure> findByEmployeeIdOrderByVersionDesc(Long employeeId);
    Optional<EmployeeSalaryStructure> findByCompanyIdAndEmployeeIdAndStatus(Long companyId, Long employeeId, String status);
}
