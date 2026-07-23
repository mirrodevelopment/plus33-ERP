/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.controller
 * File              : ShiftController.java
 * Purpose           : REST Controller exposing API endpoints for Shift & Headcount Management
 * Version           : 0.0.1-SNAPSHOT
 ******************************************************************************/

package com.plus33.erp.workforce.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.workforce.entity.Announcement;
import com.plus33.erp.workforce.entity.Shift;
import com.plus33.erp.workforce.repository.AnnouncementRepository;
import com.plus33.erp.workforce.repository.ShiftRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * REST Controller for managing employee shifts, shift types, timings, and headcount limits.
 */
@RestController
@RequestMapping("/api/v1/shifts")
public class ShiftController {

    private final ShiftRepository shiftRepository;
    private final CompanyRepository companyRepository;
    private final AnnouncementRepository announcementRepository;

    public ShiftController(ShiftRepository shiftRepository,
                           CompanyRepository companyRepository,
                           AnnouncementRepository announcementRepository) {
        this.shiftRepository = shiftRepository;
        this.companyRepository = companyRepository;
        this.announcementRepository = announcementRepository;
    }

    private Map<String, Object> mapShiftToMap(Shift s) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", s.getId());
        m.put("code", s.getCode());
        m.put("name", s.getName());
        m.put("shiftType", s.getShiftType() != null ? s.getShiftType() : "CUSTOM");
        m.put("startTime", s.getStartTime() != null ? s.getStartTime().toString() : "");
        m.put("endTime", s.getEndTime() != null ? s.getEndTime().toString() : "");
        m.put("breakMinutes", s.getBreakMinutes() != null ? s.getBreakMinutes() : 0);
        m.put("minEmployees", s.getMinEmployees() != null ? s.getMinEmployees() : 2);
        m.put("maxEmployees", s.getMaxEmployees() != null ? s.getMaxEmployees() : 8);
        m.put("overnight", Boolean.TRUE.equals(s.getOvernight()));
        m.put("active", Boolean.TRUE.equals(s.getActive()));
        return m;
    }

    private void notifySupervisors(String action, String shiftName, String shiftCode) {
        try {
            Announcement announcement = new Announcement();
            announcement.setTitle("Shift Schedule Updated: " + shiftName);
            announcement.setContent(String.format("Store Admin has %s the shift '%s' (%s). Shift Supervisors please review the Shift Planner for updated staff headcount limits and operational timings.", action, shiftName, shiftCode));
            announcement.setPriority("IMPORTANT");
            announcement.setPublisher("Store Administration");
            announcement.setCreatedAt(LocalDateTime.now());
            announcementRepository.save(announcement);
        } catch (Exception ignored) {
            // Non-blocking notification fallback
        }
    }

    private void ensureDefaultShifts() {
        if (shiftRepository.count() == 0) {
            Company company = companyRepository.findAll().stream().findFirst()
                    .orElseGet(() -> {
                        Company c = new Company();
                        c.setCode("PLUS33_COFFEE");
                        c.setName("PLUS33 Coffee");
                        c.setLegalName("PLUS33 Coffee SAS");
                        c.setCountryCode("FR");
                        c.setActive(true);
                        c.setCreatedAt(LocalDateTime.now());
                        c.setUpdatedAt(LocalDateTime.now());
                        return companyRepository.save(c);
                    });

            Shift morn = new Shift();
            morn.setCode("SHIFT_MORN");
            morn.setName("Morning Shift");
            morn.setCompany(company);
            morn.setStartTime(LocalTime.of(6, 0));
            morn.setEndTime(LocalTime.of(14, 0));
            morn.setBreakMinutes(30);
            morn.setOvernight(false);
            morn.setActive(true);
            morn.setCreatedAt(LocalDateTime.now());
            morn.setUpdatedAt(LocalDateTime.now());
            shiftRepository.save(morn);

            Shift aft = new Shift();
            aft.setCode("SHIFT_AFT");
            aft.setName("Afternoon Shift");
            aft.setCompany(company);
            aft.setStartTime(LocalTime.of(14, 0));
            aft.setEndTime(LocalTime.of(22, 0));
            aft.setBreakMinutes(30);
            aft.setOvernight(false);
            aft.setActive(true);
            aft.setCreatedAt(LocalDateTime.now());
            aft.setUpdatedAt(LocalDateTime.now());
            shiftRepository.save(aft);

            Shift nght = new Shift();
            nght.setCode("SHIFT_NGHT");
            nght.setName("Night Shift");
            nght.setCompany(company);
            nght.setStartTime(LocalTime.of(22, 0));
            nght.setEndTime(LocalTime.of(6, 0));
            nght.setBreakMinutes(30);
            nght.setOvernight(true);
            nght.setActive(true);
            nght.setCreatedAt(LocalDateTime.now());
            nght.setUpdatedAt(LocalDateTime.now());
            shiftRepository.save(nght);
        }
    }

    /**
     * GET /api/v1/shifts
     * Returns a list of all configured shifts.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getShifts() {
        ensureDefaultShifts();
        List<Shift> shifts = shiftRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();
        
        shifts.sort(Comparator.comparing(Shift::getId));

        for (Shift s : shifts) {
            result.add(mapShiftToMap(s));
        }
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * POST /api/v1/shifts
     * Creates a new shift schedule.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> createShift(@RequestBody Map<String, Object> payload) {
        String name = (String) payload.get("name");
        String code = (String) payload.get("code");
        String shiftType = (String) payload.getOrDefault("shiftType", "CUSTOM");
        String startTimeStr = (String) payload.get("startTime");
        String endTimeStr = (String) payload.get("endTime");

        if (name == null || name.trim().isEmpty() || code == null || code.trim().isEmpty()
                || startTimeStr == null || endTimeStr == null) {
            throw new BusinessException("Shift name, code, start time, and end time are required.");
        }

        if (shiftRepository.findByCode(code.trim().toUpperCase()).isPresent()) {
            throw new BusinessException("Shift with code '" + code.trim().toUpperCase() + "' already exists.");
        }

        Company company = companyRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new BusinessException("No active company found for shift setup."));

        LocalTime startTime = LocalTime.parse(startTimeStr);
        LocalTime endTime = LocalTime.parse(endTimeStr);
        Integer breakMinutes = payload.containsKey("breakMinutes") ? ((Number) payload.get("breakMinutes")).intValue() : 30;
        Integer minEmployees = payload.containsKey("minEmployees") ? ((Number) payload.get("minEmployees")).intValue() : 2;
        Integer maxEmployees = payload.containsKey("maxEmployees") ? ((Number) payload.get("maxEmployees")).intValue() : 8;

        Shift s = new Shift();
        s.setCode(code.trim().toUpperCase());
        s.setName(name.trim());
        s.setCompany(company);
        s.setShiftType(shiftType);
        s.setStartTime(startTime);
        s.setEndTime(endTime);
        s.setBreakMinutes(breakMinutes);
        s.setMinEmployees(minEmployees);
        s.setMaxEmployees(maxEmployees);
        s.setOvernight(endTime.isBefore(startTime));
        s.setActive(true);

        shiftRepository.save(s);
        notifySupervisors("created", s.getName(), s.getCode());

        return ResponseEntity.ok(ApiResponse.success("Shift schedule created successfully", mapShiftToMap(s)));
    }

    /**
     * PUT /api/v1/shifts/{id}
     * Updates an existing shift timing and headcount limits.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> updateShift(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {
        Shift s = shiftRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Shift not found"));

        try {
            if (payload.containsKey("name") && payload.get("name") != null) {
                s.setName((String) payload.get("name"));
            }
            if (payload.containsKey("shiftType") && payload.get("shiftType") != null) {
                s.setShiftType((String) payload.get("shiftType"));
            }
            if (payload.containsKey("startTime") && payload.get("startTime") != null) {
                s.setStartTime(LocalTime.parse((String) payload.get("startTime")));
            }
            if (payload.containsKey("endTime") && payload.get("endTime") != null) {
                s.setEndTime(LocalTime.parse((String) payload.get("endTime")));
            }
            if (payload.containsKey("breakMinutes") && payload.get("breakMinutes") != null) {
                s.setBreakMinutes(((Number) payload.get("breakMinutes")).intValue());
            }
            if (payload.containsKey("minEmployees") && payload.get("minEmployees") != null) {
                s.setMinEmployees(((Number) payload.get("minEmployees")).intValue());
            }
            if (payload.containsKey("maxEmployees") && payload.get("maxEmployees") != null) {
                s.setMaxEmployees(((Number) payload.get("maxEmployees")).intValue());
            }
            if (payload.containsKey("active") && payload.get("active") != null) {
                s.setActive((Boolean) payload.get("active"));
            }

            s.setOvernight(s.getEndTime().isBefore(s.getStartTime()));
            s.setUpdatedAt(LocalDateTime.now());

            shiftRepository.save(s);
            notifySupervisors("updated", s.getName(), s.getCode());
        } catch (Exception e) {
            throw new BusinessException("Failed to update shift details: " + e.getMessage());
        }

        return ResponseEntity.ok(ApiResponse.success(mapShiftToMap(s)));
    }

    /**
     * DELETE /api/v1/shifts/{id}
     * Deletes or deactivates a shift schedule.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteShift(@PathVariable Long id) {
        Shift s = shiftRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Shift not found"));

        try {
            shiftRepository.delete(s);
            notifySupervisors("deleted", s.getName(), s.getCode());
            return ResponseEntity.ok(ApiResponse.success("Shift schedule deleted successfully", null));
        } catch (Exception e) {
            s.setActive(false);
            s.setUpdatedAt(LocalDateTime.now());
            shiftRepository.save(s);
            notifySupervisors("deactivated", s.getName(), s.getCode());
            return ResponseEntity.ok(ApiResponse.success("Shift schedule has active records and was deactivated", null));
        }
    }
}

