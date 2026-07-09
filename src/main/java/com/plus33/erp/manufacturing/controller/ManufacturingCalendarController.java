/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.controller
 * File              : ManufacturingCalendarController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ManufacturingCalendarController
 * Related Service   : ManufacturingCalendarControllerService, ManufacturingCalendarControllerServiceImpl
 * Related Repository: ManufacturingCalendarControllerRepository
 * Related Entity    : ManufacturingCalendarController
 * Related DTO       : ApiResponse, CalendarDto, CalendarExceptionDto, CreateCalendarRequest, CreateExceptionRequest
 * Related Mapper    : ManufacturingCalendarControllerMapper
 * Related DB Table  : manufacturing_calendar_controllers
 * Related REST APIs : POST /api/v1/manufacturing/calendars, POST /api/v1/manufacturing/calendars/{calendarId}/shifts, POST /api/v1/manufacturing/calendars/{calendarId}/exceptions, GET /api/v1/manufacturing/calendars/{calendarId}
 * Depends On        : Common Module
 * Used By           : Manufacturing Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Manufacturing Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/manufacturing/calendars, POST /api/v1/manufacturing/calendars/{calendarId}/shifts, POST /api/v1/manufacturing/calendars/{calendarId}/exceptions, GET /api/v1/manufacturing/calendars/{calendarId}
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ManufacturingCalendarController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to ManufacturingCalendarService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> ManufacturingCalendarController.endpoint()
 *   --> ManufacturingCalendarService.method()
 *   --> ManufacturingCalendarRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/manufacturing/calendars, POST /api/v1/manufacturing/calendars/{calendarId}/shifts, POST /api/v1/manufacturing/calendars/{calendarId}/exceptions, GET /api/v1/manufacturing/calendars/{calendarId}, GET /api/v1/manufacturing/calendars/company/{companyId}</p>
 * <p><b>Module Deps      :</b> Common, Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/manufacturing/calendars")
@Tag(name = "Manufacturing Calendar Management", description = "REST APIs for configuring calendars, shifts, work center templates, and holiday exceptions")
public class ManufacturingCalendarController {

    private final ManufacturingCalendarService calendarService;

    public ManufacturingCalendarController(ManufacturingCalendarService calendarService) {
        this.calendarService = calendarService;
    }

    /**
     * Creates a new calendar and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('MANUFACTURING_ADMIN')")
    @Operation(summary = "Create Calendar", description = "Creates a resource calendar (PLANT or Work Center specific)")
    public ResponseEntity<ApiResponse<CalendarDto>> createCalendar(@Valid @RequestBody CreateCalendarRequest request) {
        CalendarDto dto = calendarService.createCalendar(request);
        return new ResponseEntity<>(ApiResponse.success("Calendar created successfully", dto), HttpStatus.CREATED);
    }

    /**
     * Creates a new shift and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param calendarId the calendarId input value
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{calendarId}/shifts")
    @PreAuthorize("hasAuthority('MANUFACTURING_ADMIN')")
    @Operation(summary = "Add Shift Template", description = "Adds a working shift schedule to a calendar")
    public ResponseEntity<ApiResponse<ShiftDto>> addShift(@PathVariable Long calendarId, @Valid @RequestBody CreateShiftRequest request) {
        ShiftDto dto = calendarService.addShift(calendarId, request);
        return new ResponseEntity<>(ApiResponse.success("Shift added successfully", dto), HttpStatus.CREATED);
    }

    /**
     * Creates a new exception and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param calendarId the calendarId input value
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{calendarId}/exceptions")
    @PreAuthorize("hasAuthority('MANUFACTURING_ADMIN')")
    @Operation(summary = "Add Calendar Exception", description = "Adds a planned downtime, maintenance, or holiday exception to a calendar")
    public ResponseEntity<ApiResponse<CalendarExceptionDto>> addException(@PathVariable Long calendarId, @Valid @RequestBody CreateExceptionRequest request) {
        CalendarExceptionDto dto = calendarService.addException(calendarId, request);
        return new ResponseEntity<>(ApiResponse.success("Exception added successfully", dto), HttpStatus.CREATED);
    }

    /**
     * Retrieves a single calendar by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param calendarId the calendarId input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{calendarId}")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW') or hasAuthority('MANUFACTURING_ADMIN')")
    @Operation(summary = "Get Calendar by ID", description = "Retrieves details of a calendar with shifts and exceptions")
    public ResponseEntity<ApiResponse<CalendarDto>> getCalendarById(@PathVariable Long calendarId) {
        CalendarDto dto = calendarService.getCalendarById(calendarId);
        return ResponseEntity.ok(ApiResponse.success("Calendar retrieved successfully", dto));
    }

    /**
     * Retrieves calendars by company data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW') or hasAuthority('MANUFACTURING_ADMIN')")
    @Operation(summary = "Get Calendars by Company", description = "Retrieves all calendars for a given company")
    public ResponseEntity<ApiResponse<List<CalendarDto>>> getCalendarsByCompany(@PathVariable Long companyId) {
        List<CalendarDto> dtoList = calendarService.getCalendarsByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Calendars retrieved successfully", dtoList));
    }
}