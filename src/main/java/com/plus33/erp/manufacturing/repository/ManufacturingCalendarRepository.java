package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ManufacturingCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ManufacturingCalendarRepository extends JpaRepository<ManufacturingCalendar, Long> {
    List<ManufacturingCalendar> findByCompanyId(Long companyId);
    Optional<ManufacturingCalendar> findByCompanyIdAndCalendarTypeAndCode(Long companyId, String calendarType, String code);
    List<ManufacturingCalendar> findByCompanyIdAndReferenceTypeAndReferenceId(Long companyId, String referenceType, Long referenceId);
}
