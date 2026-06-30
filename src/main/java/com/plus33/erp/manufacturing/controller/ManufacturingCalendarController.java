package com.plus33.erp.manufacturing.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.manufacturing.dto.*;
import com.plus33.erp.manufacturing.service.ManufacturingCalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manufacturing/calendars")
@Tag(name = "Manufacturing Calendar Management", description = "REST APIs for configuring calendars, shifts, work center templates, and holiday exceptions")
public class ManufacturingCalendarController {

    private final ManufacturingCalendarService calendarService;

    public ManufacturingCalendarController(ManufacturingCalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANUFACTURING_ADMIN')")
    @Operation(summary = "Create Calendar", description = "Creates a resource calendar (PLANT or Work Center specific)")
    public ResponseEntity<ApiResponse<CalendarDto>> createCalendar(@Valid @RequestBody CreateCalendarRequest request) {
        CalendarDto dto = calendarService.createCalendar(request);
        return new ResponseEntity<>(ApiResponse.success("Calendar created successfully", dto), HttpStatus.CREATED);
    }

    @PostMapping("/{calendarId}/shifts")
    @PreAuthorize("hasAuthority('MANUFACTURING_ADMIN')")
    @Operation(summary = "Add Shift Template", description = "Adds a working shift schedule to a calendar")
    public ResponseEntity<ApiResponse<ShiftDto>> addShift(@PathVariable Long calendarId, @Valid @RequestBody CreateShiftRequest request) {
        ShiftDto dto = calendarService.addShift(calendarId, request);
        return new ResponseEntity<>(ApiResponse.success("Shift added successfully", dto), HttpStatus.CREATED);
    }

    @PostMapping("/{calendarId}/exceptions")
    @PreAuthorize("hasAuthority('MANUFACTURING_ADMIN')")
    @Operation(summary = "Add Calendar Exception", description = "Adds a planned downtime, maintenance, or holiday exception to a calendar")
    public ResponseEntity<ApiResponse<CalendarExceptionDto>> addException(@PathVariable Long calendarId, @Valid @RequestBody CreateExceptionRequest request) {
        CalendarExceptionDto dto = calendarService.addException(calendarId, request);
        return new ResponseEntity<>(ApiResponse.success("Exception added successfully", dto), HttpStatus.CREATED);
    }

    @GetMapping("/{calendarId}")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW') or hasAuthority('MANUFACTURING_ADMIN')")
    @Operation(summary = "Get Calendar by ID", description = "Retrieves details of a calendar with shifts and exceptions")
    public ResponseEntity<ApiResponse<CalendarDto>> getCalendarById(@PathVariable Long calendarId) {
        CalendarDto dto = calendarService.getCalendarById(calendarId);
        return ResponseEntity.ok(ApiResponse.success("Calendar retrieved successfully", dto));
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW') or hasAuthority('MANUFACTURING_ADMIN')")
    @Operation(summary = "Get Calendars by Company", description = "Retrieves all calendars for a given company")
    public ResponseEntity<ApiResponse<List<CalendarDto>>> getCalendarsByCompany(@PathVariable Long companyId) {
        List<CalendarDto> dtoList = calendarService.getCalendarsByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Calendars retrieved successfully", dtoList));
    }
}
