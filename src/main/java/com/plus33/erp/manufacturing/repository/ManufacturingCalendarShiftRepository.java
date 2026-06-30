package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ManufacturingCalendarShift;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ManufacturingCalendarShiftRepository extends JpaRepository<ManufacturingCalendarShift, Long> {
    List<ManufacturingCalendarShift> findByCalendarId(Long calendarId);
    List<ManufacturingCalendarShift> findByCalendarIdAndDayOfWeek(Long calendarId, Integer dayOfWeek);
}
