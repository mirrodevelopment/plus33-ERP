/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.service
 * File              : ProductService.java
 * Purpose           : Service interface contract defining the API for Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductController
 * Related Service   : ProductService, ProductServiceImpl
 * Related Repository: ProductRepository
 * Related Entity    : Product
 * Related DTO       : PageResponse, ProductRequest, ProductResponse, ProductSearchRequest, searchRequest
 * Related Mapper    : ProductMapper
 * Related DB Table  : products
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Inventory Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.inventory.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.inventory.dto.ProductRequest;
import com.plus33.erp.inventory.dto.ProductResponse;
import com.plus33.erp.inventory.dto.ProductSearchRequest;
import org.springframework.data.domain.Pageable;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code ProductService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> Common, Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(Long id, ProductRequest request);
    ProductResponse getProductById(Long id);
    PageResponse<ProductResponse> searchProducts(ProductSearchRequest searchRequest, Pageable pageable);
    ProductResponse activateProduct(Long id);
    ProductResponse deactivateProduct(Long id);
    void deleteProduct(Long id);
}
