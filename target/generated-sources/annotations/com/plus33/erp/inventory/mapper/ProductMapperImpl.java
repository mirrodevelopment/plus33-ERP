package com.plus33.erp.inventory.mapper;

import com.plus33.erp.inventory.dto.ProductRequest;
import com.plus33.erp.inventory.dto.ProductResponse;
import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.ProductCategory;
import com.plus33.erp.inventory.entity.UnitOfMeasure;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-14T10:25:47+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toEntity(ProductRequest request) {
        if ( request == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.code( request.code() );
        product.name( request.name() );
        product.productType( request.productType() );
        product.reorderLevel( request.reorderLevel() );
        product.active( request.active() );

        return product.build();
    }

    @Override
    public ProductResponse toResponse(Product entity) {
        if ( entity == null ) {
            return null;
        }

        Long categoryId = null;
        String categoryName = null;
        Long unitId = null;
        String unitCode = null;
        Long id = null;
        String code = null;
        String name = null;
        String productType = null;
        BigDecimal reorderLevel = null;
        Boolean active = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        categoryId = entityCategoryId( entity );
        categoryName = entityCategoryName( entity );
        unitId = entityUnitId( entity );
        unitCode = entityUnitCode( entity );
        id = entity.getId();
        code = entity.getCode();
        name = entity.getName();
        productType = entity.getProductType();
        reorderLevel = entity.getReorderLevel();
        active = entity.getActive();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();

        ProductResponse productResponse = new ProductResponse( id, code, name, categoryId, categoryName, unitId, unitCode, productType, reorderLevel, active, createdAt, updatedAt );

        return productResponse;
    }

    @Override
    public void updateEntity(ProductRequest request, Product entity) {
        if ( request == null ) {
            return;
        }

        entity.setCode( request.code() );
        entity.setName( request.name() );
        entity.setProductType( request.productType() );
        entity.setReorderLevel( request.reorderLevel() );
        entity.setActive( request.active() );
    }

    private Long entityCategoryId(Product product) {
        ProductCategory category = product.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getId();
    }

    private String entityCategoryName(Product product) {
        ProductCategory category = product.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getName();
    }

    private Long entityUnitId(Product product) {
        UnitOfMeasure unit = product.getUnit();
        if ( unit == null ) {
            return null;
        }
        return unit.getId();
    }

    private String entityUnitCode(Product product) {
        UnitOfMeasure unit = product.getUnit();
        if ( unit == null ) {
            return null;
        }
        return unit.getCode();
    }
}
