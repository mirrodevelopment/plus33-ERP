/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.controller
 * File              : CustomerController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerController
 * Related Service   : CustomerControllerService, CustomerControllerServiceImpl
 * Related Repository: CustomerControllerRepository
 * Related Entity    : CustomerController
 * Related DTO       : ApiResponse, CustomerRequest, CustomerResponse, CustomerSearchRequest, PageRequest
 * Related Mapper    : CustomerControllerMapper
 * Related DB Table  : customer_controllers
 * Related REST APIs : POST /api/v1/customers, GET /api/v1/customers/{id}, GET /api/v1/customers, PUT /api/v1/customers/{id}
 * Depends On        : Common Module
 * Used By           : Sales Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Sales Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/customers, GET /api/v1/customers/{id}, GET /api/v1/customers, PUT /api/v1/customers/{id}
 ******************************************************************************/
package com.plus33.erp.sales.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.sales.dto.CustomerRequest;
import com.plus33.erp.sales.dto.CustomerResponse;
import com.plus33.erp.sales.dto.CustomerSearchRequest;
import com.plus33.erp.sales.entity.CustomerStatus;
import com.plus33.erp.sales.entity.CustomerType;
import com.plus33.erp.sales.service.CustomerService;
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
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to CustomerService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> CustomerController.endpoint()
 *   --> CustomerService.method()
 *   --> CustomerRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/customers, GET /api/v1/customers/{id}, GET /api/v1/customers, PUT /api/v1/customers/{id}, PATCH /api/v1/customers/{id}/activate</p>
 * <p><b>Module Deps      :</b> Common, Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/customers")
@Tag(name = "Customer Management", description = "REST APIs for managing sales customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Creates a new customer and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER_CREATE')")
    @Operation(summary = "Create a new customer", description = "Adds a new customer scoped to a company. Code and email must be unique within the company.")
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(@Valid @RequestBody CustomerRequest request) {
        CustomerResponse response = customerService.createCustomer(request);
        return new ResponseEntity<>(ApiResponse.success("Customer created successfully", response), HttpStatus.CREATED);
    }

    /**
     * Retrieves a single customer by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_VIEW')")
    @Operation(summary = "Get customer by ID", description = "Retrieves details of a customer by its primary key.")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(@PathVariable Long id) {
        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success("Customer retrieved successfully", response));
    }

    /**
     * Returns a filtered paginated list of customers records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @GetMapping
    @PreAuthorize("hasAuthority('CUSTOMER_VIEW')")
    @Operation(summary = "Search customers", description = "Performs dynamic searches and pagination filters for customers.")
    public ResponseEntity<ApiResponse<PageResponse<CustomerResponse>>> searchCustomers(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) CustomerType customerType,
            @RequestParam(required = false) String pricingTier,
            @RequestParam(required = false) CustomerStatus status,
            @RequestParam(required = false) Integer paymentTermsDays,
            @RequestParam(required = false) Boolean activeOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        CustomerSearchRequest searchRequest = new CustomerSearchRequest(
                query, companyId, customerType, pricingTier, status, paymentTermsDays, activeOnly
        );
        PageResponse<CustomerResponse> response = customerService.searchCustomers(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Customers retrieved successfully", response));
    }

    /**
     * Updates an existing customer record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_UPDATE')")
    @Operation(summary = "Update customer details", description = "Modifies details of an active customer by ID.")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request
    ) {
        CustomerResponse response = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(ApiResponse.success("Customer updated successfully", response));
    }

    /**
     * Performs the activateCustomer operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('CUSTOMER_ACTIVATE')")
    @Operation(summary = "Activate customer", description = "Activates a suspended, blocked, or inactive customer.")
    public ResponseEntity<ApiResponse<CustomerResponse>> activateCustomer(@PathVariable Long id) {
        CustomerResponse response = customerService.activateCustomer(id);
        return ResponseEntity.ok(ApiResponse.success("Customer activated successfully", response));
    }

    /**
     * Performs the deactivateCustomer operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('CUSTOMER_DEACTIVATE')")
    @Operation(summary = "Deactivate customer", description = "Deactivates a customer record by setting status to INACTIVE.")
    public ResponseEntity<ApiResponse<CustomerResponse>> deactivateCustomer(@PathVariable Long id) {
        CustomerResponse response = customerService.deactivateCustomer(id);
        return ResponseEntity.ok(ApiResponse.success("Customer deactivated successfully", response));
    }

    /**
     * Permanently deletes the customer from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_DELETE')")
    @Operation(summary = "Soft delete customer", description = "Soft deletes a customer by changing their status to INACTIVE.")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success("Customer deleted successfully", null));
    }
}