package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.HolidayCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HolidayCalendarRepository extends JpaRepository<HolidayCalendar, Long> {
    Optional<HolidayCalendar> findByPolicyGroupCodeAndYear(String policyGroupCode, Integer year);
}
