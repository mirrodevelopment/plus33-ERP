/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.controller
 * File              : EmployeeController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeeController
 * Related Service   : EmployeeControllerService, EmployeeControllerServiceImpl
 * Related Repository: EmployeeControllerRepository
 * Related Entity    : EmployeeController
 * Related DTO       : ApiResponse, EmployeeRequest, EmployeeResponse, EmployeeSearchRequest, PageRequest
 * Related Mapper    : EmployeeControllerMapper
 * Related DB Table  : employee_controllers
 * Related REST APIs : POST /api/v1/employees, GET /api/v1/employees/{id}, GET /api/v1/employees, PUT /api/v1/employees/{id}
 * Depends On        : Common Module
 * Used By           : Workforce Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Workforce Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/employees, GET /api/v1/employees/{id}, GET /api/v1/employees, PUT /api/v1/employees/{id}
 ******************************************************************************/
package com.plus33.erp.workforce.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.workforce.dto.EmployeeRequest;
import com.plus33.erp.workforce.dto.EmployeeResponse;
import com.plus33.erp.workforce.dto.EmployeeSearchRequest;
import com.plus33.erp.workforce.service.EmployeeService;
import com.plus33.erp.workforce.entity.EmployeeUploadDocument;
import com.plus33.erp.workforce.repository.EmployeeUploadDocumentRepository;
import java.util.List;
import java.util.Optional;
import com.plus33.erp.common.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code EmployeeController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to EmployeeService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> EmployeeController.endpoint()
 *   --> EmployeeService.method()
 *   --> EmployeeRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/employees, GET /api/v1/employees/{id}, GET /api/v1/employees, PUT /api/v1/employees/{id}, DELETE /api/v1/employees/{id}</p>
 * <p><b>Module Deps      :</b> Common, Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employee Management", description = "REST APIs for managing workforce employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeUploadDocumentRepository employeeUploadDocumentRepository;

    public EmployeeController(EmployeeService employeeService, EmployeeUploadDocumentRepository employeeUploadDocumentRepository) {
        this.employeeService = employeeService;
        this.employeeUploadDocumentRepository = employeeUploadDocumentRepository;
    }

    /**
     * Creates a new employee and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('EMPLOYEE_CREATE')")
    @Operation(summary = "Create a new employee", description = "Adds a new employee profile. Code and email must be unique within the company.")
    public ResponseEntity<ApiResponse<EmployeeResponse>> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        EmployeeResponse response = employeeService.createEmployee(request);
        return new ResponseEntity<>(ApiResponse.success("Employee created successfully", response), HttpStatus.CREATED);
    }

    /**
     * Retrieves a single employee by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_VIEW')")
    @Operation(summary = "Get employee by ID", description = "Retrieves details of an employee by primary key.")
    public ResponseEntity<ApiResponse<EmployeeResponse>> getEmployeeById(@PathVariable Long id) {
        EmployeeResponse response = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(ApiResponse.success("Employee retrieved successfully", response));
    }

    /**
     * Returns a filtered paginated list of employees records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @GetMapping
    @PreAuthorize("hasAuthority('EMPLOYEE_VIEW')")
    @Operation(summary = "Search employees", description = "Performs dynamic searches and pagination filters for employees.")
    public ResponseEntity<ApiResponse<PageResponse<EmployeeResponse>>> searchEmployees(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String designation,
            @RequestParam(required = false) String employmentType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        EmployeeSearchRequest searchRequest = new EmployeeSearchRequest(
                query, companyId, regionId, storeId, active, designation, employmentType);
        PageResponse<EmployeeResponse> response = employeeService.searchEmployees(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Employees retrieved successfully", response));
    }

    /**
     * Updates an existing employee record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_UPDATE')")
    @Operation(summary = "Update employee details", description = "Modifies details of an active employee profile by ID.")
    public ResponseEntity<ApiResponse<EmployeeResponse>> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request
    ) {
        EmployeeResponse response = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(ApiResponse.success("Employee updated successfully", response));
    }

    /**
     * Permanently deletes the employee from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE_DELETE')")
    @Operation(summary = "Soft delete employee", description = "Flags active field to false and removes user regional/store assignments.")
    public ResponseEntity<ApiResponse<Void>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(ApiResponse.success("Employee deleted successfully", null));
    }

    /**
     * Performs the activateEmployee operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('EMPLOYEE_UPDATE')")
    @Operation(summary = "Activate employee", description = "Activates a soft-deleted or inactive employee profile.")
    public ResponseEntity<ApiResponse<EmployeeResponse>> activateEmployee(@PathVariable Long id) {
        EmployeeResponse response = employeeService.activateEmployee(id);
        return ResponseEntity.ok(ApiResponse.success("Employee activated successfully", response));
    }

    /**
     * Performs the deactivateEmployee operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('EMPLOYEE_UPDATE')")
    @Operation(summary = "Deactivate employee", description = "Deactivates an employee profile and removes user assignments.")
    public ResponseEntity<ApiResponse<EmployeeResponse>> deactivateEmployee(@PathVariable Long id) {
        EmployeeResponse response = employeeService.deactivateEmployee(id);
        return ResponseEntity.ok(ApiResponse.success("Employee deactivated successfully", response));
    }

    /**
     * GET /api/v1/employees/{id}/documents
     * 
     * WHAT IT DOES: 
     * Retrieves the list of verification documents uploaded by a specific employee (identified by employee ID).
     * 
     * SENDS DATA: 
     * Returns JSON list of document records (EmployeeUploadDocument) back to the store admin's workforce overview.
     * 
     * STORAGE LOCATION: 
     * Queries records from the "employee_upload_documents" table in the PostgreSQL database.
     */
    @GetMapping("/{id}/documents")
    @PreAuthorize("hasAuthority('EMPLOYEE_VIEW')")
    @Operation(summary = "Get employee documents by ID", description = "Retrieves the verification documents uploaded by the specified employee.")
    public ResponseEntity<ApiResponse<List<EmployeeUploadDocument>>> getEmployeeDocuments(@PathVariable Long id) {
        List<EmployeeUploadDocument> docs = employeeUploadDocumentRepository.findByEmployeeId(id);
        return ResponseEntity.ok(ApiResponse.success("Employee documents retrieved successfully", docs));
    }

    /**
     * POST /api/v1/employees/documents/{documentId}/approve
     * 
     * WHAT IT DOES: 
     * Marks an employee's uploaded document as approved.
     * 
     * STORAGE LOCATION: 
     * Updates the 'approved' flag in the "employee_upload_documents" table in the PostgreSQL database.
     * 
     * FLOW:
     * Called by the Store Admin inside workforce.js details drawer.
     */
    @PostMapping("/documents/{documentId}/approve")
    @PreAuthorize("hasAuthority('EMPLOYEE_UPDATE')")
    @Operation(summary = "Approve employee document", description = "Marks a specific verification document as approved by the admin.")
    @org.springframework.transaction.annotation.Transactional
    public ResponseEntity<ApiResponse<EmployeeUploadDocument>> approveDocument(@PathVariable Long documentId) {
        Optional<EmployeeUploadDocument> docOpt = employeeUploadDocumentRepository.findById(documentId);
        if (docOpt.isEmpty()) {
            throw new BusinessException("Document record not found.");
        }
        EmployeeUploadDocument doc = docOpt.get();
        doc.setApproved(true);
        EmployeeUploadDocument saved = employeeUploadDocumentRepository.save(doc);
        return ResponseEntity.ok(ApiResponse.success("Document approved successfully", saved));
    }
}