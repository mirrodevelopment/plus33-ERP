/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.controller
 * File              : ProductController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductController
 * Related Service   : ProductControllerService, ProductControllerServiceImpl
 * Related Repository: ProductControllerRepository
 * Related Entity    : ProductController
 * Related DTO       : ApiResponse, PageRequest, PageResponse, ProductRequest, ProductResponse
 * Related Mapper    : ProductControllerMapper
 * Related DB Table  : product_controllers
 * Related REST APIs : POST /api/v1/products, GET /api/v1/products/{id}, GET /api/v1/products, PUT /api/v1/products/{id}
 * Depends On        : Common Module
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Inventory Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/products, GET /api/v1/products/{id}, GET /api/v1/products, PUT /api/v1/products/{id}
 ******************************************************************************/
package com.plus33.erp.inventory.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.inventory.dto.ProductRequest;
import com.plus33.erp.inventory.dto.ProductResponse;
import com.plus33.erp.inventory.dto.ProductSearchRequest;
import com.plus33.erp.inventory.service.ProductService;
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
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code ProductController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to ProductService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> ProductController.endpoint()
 *   --> ProductService.method()
 *   --> ProductRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/products, GET /api/v1/products/{id}, GET /api/v1/products, PUT /api/v1/products/{id}, PATCH /api/v1/products/{id}/activate</p>
 * <p><b>Module Deps      :</b> Common, Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product Management", description = "REST APIs for catalog product items")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Creates a new product and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('PRODUCT_CREATE')")
    @Operation(summary = "Create a new product", description = "Adds a new product to the global catalog. Codes must be globally unique.")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return new ResponseEntity<>(ApiResponse.success("Product created successfully", response), HttpStatus.CREATED);
    }

    /**
     * Retrieves a single product by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCT_VIEW')")
    @Operation(summary = "Get product by ID", description = "Retrieves details of a product by its primary key.")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success("Product retrieved successfully", response));
    }

    /**
     * Returns a filtered paginated list of products records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @GetMapping
    @PreAuthorize("hasAuthority('PRODUCT_VIEW')")
    @Operation(summary = "Search products", description = "Performs dynamic searches and pagination filters. Only returns active products by default.")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> searchProducts(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String productType,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        ProductSearchRequest searchRequest = new ProductSearchRequest(query, categoryId, productType, active);
        PageResponse<ProductResponse> response = productService.searchProducts(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Products retrieved successfully", response));
    }

    /**
     * Updates an existing product record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
    @Operation(summary = "Update product", description = "Modifies an existing active product details by ID.")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request
    ) {
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", response));
    }

    /**
     * Performs the activateProduct operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
    @Operation(summary = "Activate product", description = "Restores/activates a deactivated or soft-deleted product.")
    public ResponseEntity<ApiResponse<ProductResponse>> activateProduct(@PathVariable Long id) {
        ProductResponse response = productService.activateProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product activated successfully", response));
    }

    /**
     * Performs the deactivateProduct operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('PRODUCT_UPDATE')")
    @Operation(summary = "Deactivate product", description = "Deactivates product without complete deletion.")
    public ResponseEntity<ApiResponse<ProductResponse>> deactivateProduct(@PathVariable Long id) {
        ProductResponse response = productService.deactivateProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deactivated successfully", response));
    }

    /**
     * Permanently deletes the product from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PRODUCT_DELETE')")
    @Operation(summary = "Soft delete product", description = "Flags active field to false to soft delete a product.")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }
}