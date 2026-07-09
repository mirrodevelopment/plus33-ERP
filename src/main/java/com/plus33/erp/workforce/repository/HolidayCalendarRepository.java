package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.HolidayCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface HolidayCalendarRepository extends JpaRepository<HolidayCalendar, Long> {

    List<HolidayCalendar> findByCountryCodeAndHolidayYear(String countryCode, Integer year);

    boolean existsByCountryCodeAndHolidayDate(String countryCode, LocalDate holidayDate);

    List<HolidayCalendar> findByCountryCodeAndHolidayDateBetween(
            String countryCode, LocalDate startDate, LocalDate endDate);
}
