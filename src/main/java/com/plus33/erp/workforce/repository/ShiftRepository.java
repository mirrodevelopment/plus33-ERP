package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

    List<Shift> findByCompanyId(Long companyId);

    Optional<Shift> findByCode(String code);
}
