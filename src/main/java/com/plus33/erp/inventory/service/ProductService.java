package com.plus33.erp.inventory.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.inventory.dto.ProductRequest;
import com.plus33.erp.inventory.dto.ProductResponse;
import com.plus33.erp.inventory.dto.ProductSearchRequest;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(Long id, ProductRequest request);
    ProductResponse getProductById(Long id);
    PageResponse<ProductResponse> searchProducts(ProductSearchRequest searchRequest, Pageable pageable);
    ProductResponse activateProduct(Long id);
    ProductResponse deactivateProduct(Long id);
    void deleteProduct(Long id);
}
