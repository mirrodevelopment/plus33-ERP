/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.controller
 * File              : EmployeeSelfServiceController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeeSelfServiceController
 * Related Service   : EmployeeSelfServiceControllerService, EmployeeSelfServiceControllerServiceImpl
 * Related Repository: EmployeeSelfServiceControllerRepository
 * Related Entity    : EmployeeSelfServiceController
 * Related DTO       : PayslipResponse
 * Related Mapper    : EmployeeSelfServiceControllerMapper
 * Related DB Table  : employee_self_service_controllers
 * Related REST APIs : GET /api/v2/employee-self-service/payslip/{runId}/{employeeId}
 * Depends On        : None
 * Used By           : Workforce Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Workforce Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: GET /api/v2/employee-self-service/payslip/{runId}/{employeeId}
 ******************************************************************************/
package com.plus33.erp.workforce.controller;

import com.plus33.erp.workforce.dto.PayslipResponse;
import com.plus33.erp.workforce.service.EmployeeSelfService;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import com.plus33.erp.workforce.repository.EmployeeRepository;
import com.plus33.erp.workforce.entity.Employee;
import com.plus33.erp.workforce.entity.EmployeeUploadDocument;
import com.plus33.erp.workforce.entity.CountryDocumentType;
import com.plus33.erp.workforce.repository.EmployeeUploadDocumentRepository;
import com.plus33.erp.workforce.repository.CountryDocumentTypeRepository;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code EmployeeSelfServiceController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to EmployeeSelfServiceService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> EmployeeSelfServiceController.endpoint()
 *   --> EmployeeSelfServiceService.method()
 *   --> EmployeeSelfServiceRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> GET /api/v2/employee-self-service/payslip/{runId}/{employeeId}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v2/employee-self-service")
public class EmployeeSelfServiceController {

    private final EmployeeSelfService employeeSelfService;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeUploadDocumentRepository employeeUploadDocumentRepository;
    private final CountryDocumentTypeRepository countryDocumentTypeRepository;

    public EmployeeSelfServiceController(EmployeeSelfService employeeSelfService,
                                         UserRepository userRepository,
                                         EmployeeRepository employeeRepository,
                                         EmployeeUploadDocumentRepository employeeUploadDocumentRepository,
                                         CountryDocumentTypeRepository countryDocumentTypeRepository) {
        this.employeeSelfService = employeeSelfService;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.employeeUploadDocumentRepository = employeeUploadDocumentRepository;
        this.countryDocumentTypeRepository = countryDocumentTypeRepository;
    }

    /**
     * GET /api/v2/employee-self-service/documents
     * 
     * WHAT IT DOES: 
     * Retrieves the metadata of all identity/onboarding documents uploaded by the authenticated employee.
     * 
     * DATA DESTINATION (SENDS DATA): 
     * Returns JSON list of document records (EmployeeUploadDocument) back to the frontend profile.js.
     * 
     * STORAGE LOCATION: 
     * Queries records from the "employee_upload_documents" table in the PostgreSQL database.
     */
    @GetMapping("/documents")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<EmployeeUploadDocument>>> getMyDocuments(Principal principal) {
        Employee emp = resolveEmployee(principal);
        List<EmployeeUploadDocument> docs = employeeUploadDocumentRepository.findByEmployeeId(emp.getId());
        return ResponseEntity.ok(ApiResponse.success("Documents retrieved successfully", docs));
    }

    /**
     * POST /api/v2/employee-self-service/documents
     * 
     * WHAT IT DOES: 
     * Saves or overwrites the metadata for an employee's uploaded document type (e.g. panCard, aadhaarCard).
     * 
     * DATA ORIGIN (RECIEVES DATA): 
     * Receives JSON payload with documentType, documentName, and filePath from profile.js.
     * 
     * STORAGE LOCATION: 
     * Deletes any existing document of the same type and commits a new EmployeeUploadDocument record
     * into the "employee_upload_documents" table in the PostgreSQL database.
     */
    @PostMapping("/documents")
    @PreAuthorize("isAuthenticated()")
    @org.springframework.transaction.annotation.Transactional
    public ResponseEntity<ApiResponse<EmployeeUploadDocument>> saveDocument(
            @RequestBody Map<String, String> request, Principal principal) {
        Employee emp = resolveEmployee(principal);
        String docType = request.get("documentType");
        String docName = request.get("documentName");
        String filePath = request.get("filePath");

        if (docType == null || docName == null || filePath == null) {
            throw new BusinessException("Missing required document properties.");
        }

        // Validate: if an approved document of this type already exists, block replacement/re-upload
        Optional<EmployeeUploadDocument> existingDocOpt = employeeUploadDocumentRepository.findByEmployeeIdAndDocumentType(emp.getId(), docType);
        if (existingDocOpt.isPresent() && existingDocOpt.get().isApproved()) {
            throw new BusinessException("An approved document of this type already exists and cannot be replaced.");
        }

        // Delete existing doc of same type if present in the database to prevent duplicate categories
        employeeUploadDocumentRepository.deleteByEmployeeIdAndDocumentType(emp.getId(), docType);

        EmployeeUploadDocument doc = new EmployeeUploadDocument();
        doc.setEmployeeId(emp.getId());
        doc.setDocumentType(docType);
        doc.setDocumentName(docName);
        doc.setFilePath(filePath);

        EmployeeUploadDocument saved = employeeUploadDocumentRepository.save(doc);
        return ResponseEntity.ok(ApiResponse.success("Document saved successfully", saved));
    }

    /**
     * DELETE /api/v2/employee-self-service/documents/{type}
     * 
     * WHAT IT DOES: 
     * Deletes a specific document type for the authenticated employee.
     * 
     * DATA REMOVAL & STORAGE SYNC: 
     * - Deletes the physical file from the local filesystem (under 'frontend/user_uploads/documents/').
     * - Removes the metadata record from the "employee_upload_documents" table in the PostgreSQL database.
     */
    @DeleteMapping("/documents/{type}")
    @PreAuthorize("isAuthenticated()")
    @org.springframework.transaction.annotation.Transactional
    public ResponseEntity<Map<String, Object>> deleteDocument(
            @PathVariable String type, Principal principal) {
        Employee emp = resolveEmployee(principal);
        
        Optional<EmployeeUploadDocument> docOpt = employeeUploadDocumentRepository.findByEmployeeIdAndDocumentType(emp.getId(), type);
        if (docOpt.isPresent()) {
            // Validate: if document is already approved, block deletion
            if (docOpt.get().isApproved()) {
                throw new BusinessException("Approved verification documents cannot be deleted.");
            }

            // Delete physical file from the local server assets directory
            java.io.File file = new java.io.File("frontend/" + docOpt.get().getFilePath());
            if (file.exists()) {
                file.delete();
            }
            // Delete record from PostgreSQL database
            employeeUploadDocumentRepository.delete(docOpt.get());
        }

        return ResponseEntity.ok(Map.of("success", true, "message", "Document deleted successfully"));
    }

    /**
     * GET /api/v2/employee-self-service/document-requirements?country=IN
     *
     * Returns the country-specific onboarding document requirements grouped by
     * category, as ordered by category_sort and doc_sort in the DB.
     *
     * Accepted country values (case-insensitive):
     *   "India"  → country_code = IN
     *   "UAE" / "United Arab Emirates" → AE
     *   anything else (France, EU states) → EU
     *
     * Response shape:
     * {
     *   "success": true,
     *   "data": [
     *     {
     *       "category": "personal",
     *       "categoryLabel": "Personal Documents",
     *       "categoryIcon": "user",
     *       "docs": [
     *         { "docKey": "personalPhoto", "docTitle": "...", "docDescription": "...", "required": true }
     *       ]
     *     }
     *   ]
     * }
     */
    @GetMapping("/document-requirements")
    @PreAuthorize("isAuthenticated()")
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getDocumentRequirements(
            @RequestParam(value = "country", defaultValue = "India") String country) {

        // Resolve country string → internal code
        // Supports full country names, ISO-2 codes, and common aliases
        String code;
        String lower = country.trim().toLowerCase();

        // ── India ──
        if (lower.equals("india") || lower.equals("in")) {
            code = "IN";

        // ── UAE ──
        } else if (lower.equals("uae") || lower.equals("ae")
                || lower.contains("arab") || lower.contains("emirates")) {
            code = "AE";

        // ── Explicit EU country names (full list) ──
        } else if (lower.equals("france") || lower.equals("fr")
                || lower.equals("germany") || lower.equals("de")
                || lower.equals("ireland") || lower.equals("ie")
                || lower.equals("spain") || lower.equals("es")
                || lower.equals("italy") || lower.equals("it")
                || lower.equals("netherlands") || lower.equals("nl")
                || lower.equals("belgium") || lower.equals("be")
                || lower.equals("portugal") || lower.equals("pt")
                || lower.equals("austria") || lower.equals("at")
                || lower.equals("sweden") || lower.equals("se")
                || lower.equals("denmark") || lower.equals("dk")
                || lower.equals("finland") || lower.equals("fi")
                || lower.equals("norway") || lower.equals("no")
                || lower.equals("switzerland") || lower.equals("ch")
                || lower.equals("luxembourg") || lower.equals("lu")
                || lower.equals("poland") || lower.equals("pl")
                || lower.equals("czech republic") || lower.equals("cz")
                || lower.equals("hungary") || lower.equals("hu")
                || lower.equals("romania") || lower.equals("ro")
                || lower.equals("greece") || lower.equals("gr")
                || lower.equals("eu")) {
            code = "EU";

        // ── Default: treat unknown countries as EU ──
        } else {
            code = "EU";
        }

        List<CountryDocumentType> rows =
                countryDocumentTypeRepository.findByCountryCodeOrderByCategorySortAscDocSortAsc(code);

        // If no rows found for derived code, fall back to IN (ensures data always returned)
        if (rows.isEmpty() && !"IN".equals(code)) {
            rows = countryDocumentTypeRepository.findByCountryCodeOrderByCategorySortAscDocSortAsc("IN");
            code = "IN (fallback)";
        }


        // Group into categories preserving insertion order
        LinkedHashMap<String, Map<String, Object>> grouped = new LinkedHashMap<>();
        for (CountryDocumentType row : rows) {
            grouped.computeIfAbsent(row.getCategory(), k -> {
                Map<String, Object> cat = new LinkedHashMap<>();
                cat.put("category",      row.getCategory());
                cat.put("categoryLabel", row.getCategoryLabel());
                cat.put("categoryIcon",  row.getCategoryIcon());
                cat.put("docs",          new ArrayList<>());
                return cat;
            });

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> docs = (List<Map<String, Object>>) grouped.get(row.getCategory()).get("docs");
            Map<String, Object> doc = new LinkedHashMap<>();
            doc.put("docKey",          row.getDocKey());
            doc.put("docTitle",        row.getDocTitle());
            doc.put("docDescription",  row.getDocDescription());
            doc.put("required",        row.isRequired());
            docs.add(doc);
        }

        List<Map<String, Object>> result = new ArrayList<>(grouped.values());
        return ResponseEntity.ok(ApiResponse.success(
                "Document requirements retrieved for country code: " + code, result));
    }

    /**
     * Retrieves all payslips for the logged-in employee.
     */
    @GetMapping("/payslips")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PayslipResponse>> getMyPayslips(Principal principal) {
        Employee emp = resolveEmployee(principal);
        return ResponseEntity.ok(employeeSelfService.getPayslipsForEmployee(emp.getId()));
    }

    /**
     * Retrieves payslip data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param runId the runId input value
     * @param employeeId the employeeId input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/payslip/{runId}/{employeeId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PayslipResponse> getPayslip(@PathVariable Long runId, @PathVariable Long employeeId, Principal principal) {
        Employee emp = resolveEmployee(principal);
        if (!emp.getId().equals(employeeId)) {
            // Also allow if they are admin/finance manager (has authority PAYROLL_VIEW)
            boolean hasAdminAuthority = org.springframework.security.core.context.SecurityContextHolder.getContext()
                    .getAuthentication().getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("PAYROLL_VIEW"));
            if (!hasAdminAuthority) {
                throw new BusinessException("Access denied. You cannot view another employee's payslip.");
            }
        }
        return ResponseEntity.ok(employeeSelfService.getPayslipForEmployee(runId, employeeId));
    }

    private Employee resolveEmployee(Principal principal) {
        if (principal == null) throw new BusinessException("Authentication required.");
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new BusinessException("User profile not found."));
        return employeeRepository.findByUserId(user.getId())
                .orElseThrow(() -> new BusinessException("Employee record not found for this user."));
    }
}