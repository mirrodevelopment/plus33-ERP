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
import com.plus33.erp.common.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

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

    public EmployeeSelfServiceController(EmployeeSelfService employeeSelfService,
                                         UserRepository userRepository,
                                         EmployeeRepository employeeRepository) {
        this.employeeSelfService = employeeSelfService;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
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