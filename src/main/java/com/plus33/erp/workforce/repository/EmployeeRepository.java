package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    Optional<Employee> findByEmployeeCode(String employeeCode);

    java.util.List<Employee> findByCompanyId(Long companyId);

    Optional<Employee> findByUserId(Long userId);

    boolean existsByCompanyIdAndEmployeeCode(Long companyId, String employeeCode);

    boolean existsByCompanyIdAndEmail(Long companyId, String email);

    boolean existsByUserId(Long userId);

    boolean existsByUserIdAndIdNot(Long userId, Long employeeId);
}
