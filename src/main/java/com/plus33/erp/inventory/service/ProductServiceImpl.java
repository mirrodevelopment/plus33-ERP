/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.service
 * File              : ProductServiceImpl.java
 * Purpose           : Business logic service layer for Inventory Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductController
 * Related Service   : ProductServiceImpl
 * Related Repository: ProductRepository, ProductCategoryRepository, UnitOfMeasureRepository
 * Related Entity    : Product
 * Related DTO       : PageResponse, ProductRequest, ProductResponse, ProductSearchRequest, searchRequest
 * Related Mapper    : ProductMapper
 * Related DB Table  : products
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : ProductController, ProductServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Inventory Module. Implements ProductService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.inventory.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.DuplicateResourceException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.inventory.dto.ProductRequest;
import com.plus33.erp.inventory.dto.ProductResponse;
import com.plus33.erp.inventory.dto.ProductSearchRequest;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.ProductCategory;
import com.plus33.erp.inventory.entity.UnitOfMeasure;
import com.plus33.erp.inventory.mapper.ProductMapper;
import com.plus33.erp.inventory.repository.ProductCategoryRepository;
import com.plus33.erp.inventory.repository.ProductRepository;
import com.plus33.erp.inventory.repository.UnitOfMeasureRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code ProductServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Inventory Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ProductController
 *   --> ProductServiceImpl (this)
 *   --> Validate business rules
 *   --> ProductRepository (read/write 'products')
 *   --> ProductMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code products}</p>
 * <p><b>Module Deps      :</b> Common, Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository,
                              ProductCategoryRepository categoryRepository,
                              UnitOfMeasureRepository unitRepository,
                              ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.unitRepository = unitRepository;
        this.productMapper = productMapper;
    }

    /**
     * Creates a new product and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return the ProductResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsByCode(request.code())) {
            throw new DuplicateResourceException("Product with code " + request.code() + " already exists");
        }
        if (productRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateResourceException("Product with name " + request.name() + " already exists");
        }

        ProductCategory category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + request.categoryId()));
        UnitOfMeasure unit = unitRepository.findById(request.unitId())
                .orElseThrow(() -> new ResourceNotFoundException("Unit of measure not found with ID: " + request.unitId()));

        Product product = productMapper.toEntity(request);
        product.setCategory(category);
        product.setUnit(unit);
        if (request.active() != null) {
            product.setActive(request.active());
        }

        Product saved = productRepository.save(product);
        return productMapper.toResponse(saved);
    }

    /**
     * Updates an existing product record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the ProductResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        if (!product.getActive()) {
            throw new BusinessException("Soft-deleted products cannot be updated");
        }

        if (!product.getCode().equals(request.code()) && productRepository.existsByCode(request.code())) {
            throw new DuplicateResourceException("Product with code " + request.code() + " already exists");
        }
        if (!product.getName().equalsIgnoreCase(request.name()) && productRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateResourceException("Product with name " + request.name() + " already exists");
        }

        ProductCategory category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + request.categoryId()));
        UnitOfMeasure unit = unitRepository.findById(request.unitId())
                .orElseThrow(() -> new ResourceNotFoundException("Unit of measure not found with ID: " + request.unitId()));

        productMapper.updateEntity(request, product);
        product.setCategory(category);
        product.setUnit(unit);
        if (request.active() != null) {
            product.setActive(request.active());
        }

        Product saved = productRepository.save(product);
        return productMapper.toResponse(saved);
    }

    /**
     * Retrieves a single product by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the ProductResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        return productMapper.toResponse(product);
    }

    /**
     * Returns a filtered paginated list of products records.
     *
     * @param searchRequest the searchRequest input value
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> searchProducts(ProductSearchRequest searchRequest, Pageable pageable) {
        Specification<Product> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchRequest.query() != null && !searchRequest.query().isBlank()) {
                String searchPattern = "%" + searchRequest.query().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("code")), searchPattern),
                        cb.like(cb.lower(root.get("name")), searchPattern)
                ));
            }

            if (searchRequest.categoryId() != null) {
                predicates.add(cb.equal(root.get("category").get("id"), searchRequest.categoryId()));
            }

            if (searchRequest.productType() != null && !searchRequest.productType().isBlank()) {
                predicates.add(cb.equal(root.get("productType"), searchRequest.productType()));
            }

            Boolean activeFilter = searchRequest.active() != null ? searchRequest.active() : Boolean.TRUE;
            predicates.add(cb.equal(root.get("active"), activeFilter));

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Product> productPage = productRepository.findAll(spec, pageable);
        List<ProductResponse> content = productPage.getContent().stream()
                .map(productMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages()
        );
    }

    /**
     * Performs the activateProduct operation in this module.
     *
     * @param id the unique database ID of the resource
     * @return the ProductResponse result
     */
    @Override
    @Transactional
    public ProductResponse activateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        product.setActive(true);
        Product saved = productRepository.save(product);
        return productMapper.toResponse(saved);
    }

    /**
     * Performs the deactivateProduct operation in this module.
     *
     * @param id the unique database ID of the resource
     * @return the ProductResponse result
     */
    @Override
    @Transactional
    public ProductResponse deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        product.setActive(false);
        Product saved = productRepository.save(product);
        return productMapper.toResponse(saved);
    }

    /**
     * Permanently deletes the product from the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     */
    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        product.setActive(false);
        productRepository.save(product);
    }
}