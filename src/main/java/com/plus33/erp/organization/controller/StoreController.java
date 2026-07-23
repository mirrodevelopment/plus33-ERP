/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.controller
 * File              : StoreController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StoreController
 * Related Service   : StoreControllerService, StoreControllerServiceImpl
 * Related Repository: StoreControllerRepository
 * Related Entity    : StoreController
 * Related DTO       : ApiResponse, PageRequest, PageResponse, searchRequest, StoreRequest
 * Related Mapper    : StoreControllerMapper
 * Related DB Table  : store_controllers
 * Related REST APIs : POST /api/v1/stores, GET /api/v1/stores/{id}, GET /api/v1/stores, PUT /api/v1/stores/{id}
 * Depends On        : Common Module
 * Used By           : Organization Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Organization Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/stores, GET /api/v1/stores/{id}, GET /api/v1/stores, PUT /api/v1/stores/{id}
 ******************************************************************************/
package com.plus33.erp.organization.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.organization.dto.StoreRequest;
import com.plus33.erp.organization.dto.StoreResponse;
import com.plus33.erp.organization.dto.StoreSearchRequest;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.repository.StoreRepository;
import com.plus33.erp.organization.service.StoreService;
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

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <b>PLUS33 Coffee ERP -- Organization Module</b>
 *
 * <p><b>Class  :</b> {@code StoreController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to StoreService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> StoreController.endpoint()
 *   --> StoreService.method()
 *   --> StoreRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/stores, GET /api/v1/stores/{id}, GET /api/v1/stores, PUT /api/v1/stores/{id}, DELETE /api/v1/stores/{id}</p>
 * <p><b>Module Deps      :</b> Common, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/stores")
@Tag(name = "Store Management", description = "REST APIs for managing organization stores")
public class StoreController {

    private final StoreService storeService;
    private final StoreRepository storeRepository;

    public StoreController(StoreService storeService, StoreRepository storeRepository) {
        this.storeService = storeService;
        this.storeRepository = storeRepository;
    }

    /**
     * Creates a new store and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('STORE_CREATE')")
    @Operation(summary = "Create a new store", description = "Adds a new store scoped to a region. Code must be unique within the company.")
    public ResponseEntity<ApiResponse<StoreResponse>> createStore(@Valid @RequestBody StoreRequest request) {
        StoreResponse response = storeService.createStore(request);
        return new ResponseEntity<>(ApiResponse.success("Store created successfully", response), HttpStatus.CREATED);
    }

    /**
     * Retrieves a single store by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('STORE_VIEW')")
    @Operation(summary = "Get store by ID", description = "Retrieves details of a store by its primary key.")
    public ResponseEntity<ApiResponse<StoreResponse>> getStoreById(@PathVariable Long id) {
        StoreResponse response = storeService.getStoreById(id);
        return ResponseEntity.ok(ApiResponse.success("Store retrieved successfully", response));
    }

    /**
     * Returns a filtered paginated list of stores records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @GetMapping
    @PreAuthorize("hasAuthority('STORE_VIEW')")
    @Operation(summary = "Search stores", description = "Performs dynamic searches and pagination filters for stores.")
    public ResponseEntity<ApiResponse<PageResponse<StoreResponse>>> searchStores(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        StoreSearchRequest searchRequest = new StoreSearchRequest(query, companyId, regionId, warehouseId, active);
        PageResponse<StoreResponse> response = storeService.searchStores(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Stores retrieved successfully", response));
    }

    /**
     * Updates an existing store record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('STORE_UPDATE')")
    @Operation(summary = "Update store details", description = "Modifies details of an active store by ID.")
    public ResponseEntity<ApiResponse<StoreResponse>> updateStore(
            @PathVariable Long id,
            @Valid @RequestBody StoreRequest request
    ) {
        StoreResponse response = storeService.updateStore(id, request);
        return ResponseEntity.ok(ApiResponse.success("Store updated successfully", response));
    }

    /**
     * Permanently deletes the store from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('STORE_DELETE')")
    @Operation(summary = "Soft delete store", description = "Flags active field to false to soft delete a store.")
    public ResponseEntity<ApiResponse<Void>> deleteStore(@PathVariable Long id) {
        storeService.deleteStore(id);
        return ResponseEntity.ok(ApiResponse.success("Store deleted successfully", null));
    }

    /**
     * Performs the activateStore operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('STORE_UPDATE')")
    @Operation(summary = "Activate store", description = "Activates an inactive store.")
    public ResponseEntity<ApiResponse<StoreResponse>> activateStore(@PathVariable Long id) {
        StoreResponse response = storeService.activateStore(id);
        return ResponseEntity.ok(ApiResponse.success("Store activated successfully", response));
    }

    /**
     * Performs the deactivateStore operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('STORE_UPDATE')")
    @Operation(summary = "Deactivate store", description = "Deactivates a store.")
    public ResponseEntity<ApiResponse<StoreResponse>> deactivateStore(@PathVariable Long id) {
        StoreResponse response = storeService.deactivateStore(id);
        return ResponseEntity.ok(ApiResponse.success("Store deactivated successfully", response));
    }

    @GetMapping("/{id}/settings")
    @PreAuthorize("hasAuthority('STORE_VIEW')")
    @Operation(summary = "Get store settings", description = "Retrieves configurations for the specified store location.")
    public ResponseEntity<ApiResponse<com.plus33.erp.organization.dto.StoreSettingResponse>> getStoreSettings(@PathVariable Long id) {
        com.plus33.erp.organization.dto.StoreSettingResponse response = storeService.getStoreSettings(id);
        return ResponseEntity.ok(ApiResponse.success("Store settings retrieved successfully", response));
    }

    @PutMapping("/{id}/settings")
    @PreAuthorize("hasAuthority('STORE_UPDATE')")
    @Operation(summary = "Update store settings", description = "Updates operational configurations for the specified store location.")
    public ResponseEntity<ApiResponse<com.plus33.erp.organization.dto.StoreSettingResponse>> updateStoreSettings(@PathVariable Long id, @Valid @RequestBody com.plus33.erp.organization.dto.StoreSettingRequest request) {
        com.plus33.erp.organization.dto.StoreSettingResponse response = storeService.updateStoreSettings(id, request);
        return ResponseEntity.ok(ApiResponse.success("Store settings updated successfully", response));
    }

    /**
     * Store Admin or Shift Supervisor sets the store's GPS location from their device.
     * Coordinates are saved permanently and persist until the next explicit update.
     *
     * <p>Body: {@code { "latitude": 48.8566, "longitude": 2.3522 }}</p>
     *
     * @param id  the store's database ID
     * @param body request body containing latitude and longitude
     * @return the updated GPS coordinates and store name
     */
    @PutMapping("/{id}/location")
    @PreAuthorize("hasAuthority('STORE_UPDATE')")
    @Operation(summary = "Set store GPS location",
            description = "Saves the store's GPS coordinates. Set once from the admin device; persists until next explicit update.")
    public ResponseEntity<ApiResponse<Map<String, Object>>> setStoreLocation(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Store not found: " + id));

        try {
            BigDecimal lat = new BigDecimal(body.getOrDefault("latitude", "").toString());
            BigDecimal lng = new BigDecimal(body.getOrDefault("longitude", "").toString());
            store.setLatitude(lat);
            store.setLongitude(lng);
            if (body.containsKey("geofenceRadiusMeters")) {
                store.setGeofenceRadiusMeters(Integer.parseInt(body.get("geofenceRadiusMeters").toString()));
            }
            storeRepository.save(store);
        } catch (NumberFormatException e) {
            throw new BusinessException("Invalid coordinates. Provide numeric latitude and longitude values.");
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("storeId", store.getId());
        result.put("storeName", store.getName());
        result.put("latitude", store.getLatitude());
        result.put("longitude", store.getLongitude());
        result.put("geofenceRadiusMeters", store.getGeofenceRadiusMeters());
        result.put("message", "Store location saved. This will remain active until you update it again.");
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping(value = "/{id}/documents", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('STORE_UPDATE')")
    @Operation(summary = "Upload store document", description = "Uploads a compliance/licensing document for the store.")
    public ResponseEntity<ApiResponse<com.plus33.erp.organization.dto.StoreDocumentResponse>> uploadStoreDocument(
            @PathVariable Long id,
            @RequestParam("documentType") String documentType,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("Uploaded file is empty.");
        }
        if (file.getSize() > 10 * 1024 * 1024) { // 10MB limit
            throw new BusinessException("File size exceeds the 10MB limit.");
        }

        try {
            java.nio.file.Path uploadDir = java.nio.file.Paths.get("storage/documents");
            if (!java.nio.file.Files.exists(uploadDir)) {
                java.nio.file.Files.createDirectories(uploadDir);
            }

            String originalName = file.getOriginalFilename();
            String extension = "";
            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf("."));
            }
            String uniqueName = java.util.UUID.randomUUID().toString() + extension;
            java.nio.file.Path targetPath = uploadDir.resolve(uniqueName);
            java.nio.file.Files.copy(file.getInputStream(), targetPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            String relativePath = "storage/documents/" + uniqueName;
            com.plus33.erp.organization.dto.StoreDocumentResponse doc = storeService.uploadDocument(id, documentType, originalName, relativePath);
            return ResponseEntity.ok(ApiResponse.success("Document uploaded successfully", doc));
        } catch (java.io.IOException e) {
            throw new BusinessException("Failed to save uploaded file: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/documents/{docId}")
    @PreAuthorize("hasAuthority('STORE_UPDATE')")
    @Operation(summary = "Delete store document", description = "Deletes a compliance/licensing document by its ID.")
    public ResponseEntity<ApiResponse<Void>> deleteStoreDocument(
            @PathVariable Long id,
            @PathVariable Long docId) {
        storeService.deleteDocument(id, docId);
        return ResponseEntity.ok(ApiResponse.success("Document deleted successfully", null));
    }
}