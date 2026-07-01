package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.DimDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DimDateRepository extends JpaRepository<DimDate, Long> {
    java.util.Optional<DimDate> findByDateKey(Integer dateKey);
    java.util.Optional<DimDate> findByFullDate(java.time.LocalDate date);
}
