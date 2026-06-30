package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ManufacturingCalendarException;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ManufacturingCalendarExceptionRepository extends JpaRepository<ManufacturingCalendarException, Long> {
    List<ManufacturingCalendarException> findByCalendarId(Long calendarId);
    List<ManufacturingCalendarException> findByCalendarIdAndExceptionDate(Long calendarId, LocalDate exceptionDate);
    List<ManufacturingCalendarException> findByCalendarIdAndExceptionDateBetween(Long calendarId, LocalDate start, LocalDate end);
}
