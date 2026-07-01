package com.plus33.erp.hcm.service;

import com.plus33.erp.hcm.entity.Roster;
import com.plus33.erp.hcm.entity.ShiftPattern;
import com.plus33.erp.hcm.repository.RosterRepository;
import com.plus33.erp.hcm.repository.ShiftPatternRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class WorkforceCapacityService {

    private final RosterRepository rosterRepository;
    private final ShiftPatternRepository shiftPatternRepository;

    public WorkforceCapacityService(RosterRepository rosterRepository, ShiftPatternRepository shiftPatternRepository) {
        this.rosterRepository = rosterRepository;
        this.shiftPatternRepository = shiftPatternRepository;
    }

    @Transactional(readOnly = true)
    public BigDecimal calculateAvailableHours(Long employeeId) {
        List<Roster> rosters = rosterRepository.findByEmployeeId(employeeId);
        BigDecimal total = BigDecimal.ZERO;
        for (Roster r : rosters) {
            ShiftPattern pattern = shiftPatternRepository.findById(r.getShiftPatternId()).orElse(null);
            if (pattern != null) {
                // Return daily share of weekly shift hours (approx. 8 hours)
                total = total.add(pattern.getWeeklyHours().divide(new BigDecimal("5"), 2, RoundingMode.HALF_UP));
            }
        }
        return total;
    }
}
