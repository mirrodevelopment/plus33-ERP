/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.controller
 * File              : ShiftController.java
 * Purpose           : REST Controller exposing API endpoints for Shift Management
 * Version           : 0.0.1-SNAPSHOT
 ******************************************************************************/

package com.plus33.erp.workforce.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.workforce.entity.Shift;
import com.plus33.erp.workforce.repository.ShiftRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.*;

/**
 * REST Controller for managing employee shifts.
 */
@RestController
@RequestMapping("/api/v1/shifts")
public class ShiftController {

    private final ShiftRepository shiftRepository;

    public ShiftController(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    /**
     * GET /api/v1/shifts
     * Returns a list of all configured shifts.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getShifts() {
        List<Shift> shifts = shiftRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        
        // Sort shifts by ID or code for consistency
        shifts.sort(Comparator.comparing(Shift::getId));

        for (Shift s : shifts) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", s.getId());
            m.put("code", s.getCode());
            m.put("name", s.getName());
            m.put("startTime", s.getStartTime() != null ? s.getStartTime().toString() : "");
            m.put("endTime", s.getEndTime() != null ? s.getEndTime().toString() : "");
            m.put("breakMinutes", s.getBreakMinutes());
            m.put("overnight", s.getOvernight());
            m.put("active", s.getActive());
            result.add(m);
        }
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * PUT /api/v1/shifts/{id}
     * Updates an existing shift timing.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateShift(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {
        Shift s = shiftRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Shift not found"));

        try {
            if (payload.containsKey("startTime")) {
                s.setStartTime(LocalTime.parse((String) payload.get("startTime")));
            }
            if (payload.containsKey("endTime")) {
                s.setEndTime(LocalTime.parse((String) payload.get("endTime")));
            }
            if (payload.containsKey("breakMinutes")) {
                s.setBreakMinutes(((Number) payload.get("breakMinutes")).intValue());
            }
            if (payload.containsKey("active")) {
                s.setActive((Boolean) payload.get("active"));
            }

            // A shift is overnight if the end time is chronologically before start time
            s.setOvernight(s.getEndTime().isBefore(s.getStartTime()));
            s.setUpdatedAt(java.time.LocalDateTime.now());

            shiftRepository.save(s);
        } catch (Exception e) {
            throw new BusinessException("Failed to update shift timings: " + e.getMessage());
        }

        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", s.getId());
        m.put("code", s.getCode());
        m.put("name", s.getName());
        m.put("startTime", s.getStartTime().toString());
        m.put("endTime", s.getEndTime().toString());
        m.put("breakMinutes", s.getBreakMinutes());
        m.put("overnight", s.getOvernight());
        m.put("active", s.getActive());

        return ResponseEntity.ok(ApiResponse.success(m));
    }
}
