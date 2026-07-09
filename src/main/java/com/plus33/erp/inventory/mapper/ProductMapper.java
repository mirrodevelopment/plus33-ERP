/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.mapper
 * File              : ProductMapper.java
 * Purpose           : MapStruct Mapper converting between entities and DTOs in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductController
 * Related Service   : ProductService, ProductServiceImpl
 * Related Repository: ProductRepository
 * Related Entity    : Product
 * Related DTO       : ProductRequest, ProductResponse, toResponse
 * Related Mapper    : ProductMapper
 * Related DB Table  : products
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : ProductService, ProductServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * MapStruct Mapper for Inventory Module. Converts JPA entities to DTOs and vice versa. Generated at compile time. Inherits GlobalMapperConfig.
 ******************************************************************************/
package com.plus33.erp.inventory.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.inventory.dto.ProductRequest;
import com.plus33.erp.inventory.dto.ProductResponse;
import com.plus33.erp.inventory.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code ProductMapper}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.mapper}</p>
 * <p><b>Layer  :</b> MapStruct Mapper: compile-time Entity to DTO conversion. No runtime reflection.</p>
 *
 * <p><b>Module Deps      :</b> Common, Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Mapper(config = GlobalMapperConfig.class)
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductRequest request);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "unitId", source = "unit.id")
    @Mapping(target = "unitCode", source = "unit.code")
    ProductResponse toResponse(Product entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(ProductRequest request, @MappingTarget Product entity);
}