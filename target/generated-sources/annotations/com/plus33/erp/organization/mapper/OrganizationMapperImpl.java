package com.plus33.erp.organization.mapper;

import com.plus33.erp.organization.dto.CompanyRequest;
import com.plus33.erp.organization.dto.CompanyResponse;
import com.plus33.erp.organization.dto.RegionRequest;
import com.plus33.erp.organization.dto.RegionResponse;
import com.plus33.erp.organization.dto.StoreRequest;
import com.plus33.erp.organization.dto.StoreResponse;
import com.plus33.erp.organization.dto.WarehouseRequest;
import com.plus33.erp.organization.dto.WarehouseResponse;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Region;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-14T10:25:48+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class OrganizationMapperImpl implements OrganizationMapper {

    @Override
    public Company toEntity(CompanyRequest request) {
        if ( request == null ) {
            return null;
        }

        Company company = new Company();

        company.setCode( request.code() );
        company.setName( request.name() );
        company.setLegalName( request.legalName() );
        company.setCountryCode( request.countryCode() );
        company.setActive( request.active() );

        return company;
    }

    @Override
    public CompanyResponse toResponse(Company entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String code = null;
        String name = null;
        String legalName = null;
        String countryCode = null;
        Boolean active = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = entity.getId();
        code = entity.getCode();
        name = entity.getName();
        legalName = entity.getLegalName();
        countryCode = entity.getCountryCode();
        active = entity.getActive();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();

        CompanyResponse companyResponse = new CompanyResponse( id, code, name, legalName, countryCode, active, createdAt, updatedAt );

        return companyResponse;
    }

    @Override
    public void updateEntity(CompanyRequest request, Company entity) {
        if ( request == null ) {
            return;
        }

        entity.setCode( request.code() );
        entity.setName( request.name() );
        entity.setLegalName( request.legalName() );
        entity.setCountryCode( request.countryCode() );
        entity.setActive( request.active() );
    }

    @Override
    public Region toEntity(RegionRequest request) {
        if ( request == null ) {
            return null;
        }

        Region region = new Region();

        region.setCode( request.code() );
        region.setName( request.name() );
        region.setDescription( request.description() );

        return region;
    }

    @Override
    public RegionResponse toResponse(Region entity) {
        if ( entity == null ) {
            return null;
        }

        Long companyId = null;
        String companyCode = null;
        Long parentId = null;
        String parentName = null;
        Long id = null;
        String code = null;
        String name = null;
        String description = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        companyId = entityCompanyId( entity );
        companyCode = entityCompanyCode( entity );
        parentId = entityParentId( entity );
        parentName = entityParentName( entity );
        id = entity.getId();
        code = entity.getCode();
        name = entity.getName();
        description = entity.getDescription();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();

        RegionResponse regionResponse = new RegionResponse( id, code, name, description, companyId, companyCode, parentId, parentName, createdAt, updatedAt );

        return regionResponse;
    }

    @Override
    public void updateEntity(RegionRequest request, Region entity) {
        if ( request == null ) {
            return;
        }

        entity.setCode( request.code() );
        entity.setName( request.name() );
        entity.setDescription( request.description() );
    }

    @Override
    public Warehouse toEntity(WarehouseRequest request) {
        if ( request == null ) {
            return null;
        }

        Warehouse warehouse = new Warehouse();

        warehouse.setCode( request.code() );
        warehouse.setName( request.name() );
        warehouse.setAddress( request.address() );
        warehouse.setPhone( request.phone() );
        warehouse.setEmail( request.email() );
        warehouse.setTimezone( request.timezone() );
        warehouse.setOpeningDate( request.openingDate() );
        warehouse.setActive( request.active() );

        return warehouse;
    }

    @Override
    public WarehouseResponse toResponse(Warehouse entity) {
        if ( entity == null ) {
            return null;
        }

        Long regionId = null;
        String regionCode = null;
        Long id = null;
        String code = null;
        String name = null;
        String address = null;
        String phone = null;
        String email = null;
        String timezone = null;
        LocalDate openingDate = null;
        Boolean active = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        regionId = entityRegionId( entity );
        regionCode = entityRegionCode( entity );
        id = entity.getId();
        code = entity.getCode();
        name = entity.getName();
        address = entity.getAddress();
        phone = entity.getPhone();
        email = entity.getEmail();
        timezone = entity.getTimezone();
        openingDate = entity.getOpeningDate();
        active = entity.getActive();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();

        WarehouseResponse warehouseResponse = new WarehouseResponse( id, code, name, address, phone, email, timezone, openingDate, regionId, regionCode, active, createdAt, updatedAt );

        return warehouseResponse;
    }

    @Override
    public void updateEntity(WarehouseRequest request, Warehouse entity) {
        if ( request == null ) {
            return;
        }

        entity.setCode( request.code() );
        entity.setName( request.name() );
        entity.setAddress( request.address() );
        entity.setPhone( request.phone() );
        entity.setEmail( request.email() );
        entity.setTimezone( request.timezone() );
        entity.setOpeningDate( request.openingDate() );
        entity.setActive( request.active() );
    }

    @Override
    public Store toEntity(StoreRequest request) {
        if ( request == null ) {
            return null;
        }

        Store store = new Store();

        store.setCode( request.code() );
        store.setName( request.name() );
        store.setAddress( request.address() );
        store.setPhone( request.phone() );
        store.setEmail( request.email() );
        store.setTimezone( request.timezone() );
        store.setOpeningDate( request.openingDate() );
        store.setActive( request.active() );

        return store;
    }

    @Override
    public StoreResponse toResponse(Store entity) {
        if ( entity == null ) {
            return null;
        }

        Long regionId = null;
        String regionCode = null;
        Long warehouseId = null;
        String warehouseCode = null;
        Long id = null;
        String code = null;
        String name = null;
        String address = null;
        String phone = null;
        String email = null;
        String timezone = null;
        LocalDate openingDate = null;
        Boolean active = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        regionId = entityRegionId1( entity );
        regionCode = entityRegionCode1( entity );
        warehouseId = entityWarehouseId( entity );
        warehouseCode = entityWarehouseCode( entity );
        id = entity.getId();
        code = entity.getCode();
        name = entity.getName();
        address = entity.getAddress();
        phone = entity.getPhone();
        email = entity.getEmail();
        timezone = entity.getTimezone();
        openingDate = entity.getOpeningDate();
        active = entity.getActive();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();

        StoreResponse storeResponse = new StoreResponse( id, code, name, address, phone, email, timezone, openingDate, regionId, regionCode, warehouseId, warehouseCode, active, createdAt, updatedAt );

        return storeResponse;
    }

    @Override
    public void updateEntity(StoreRequest request, Store entity) {
        if ( request == null ) {
            return;
        }

        entity.setCode( request.code() );
        entity.setName( request.name() );
        entity.setAddress( request.address() );
        entity.setPhone( request.phone() );
        entity.setEmail( request.email() );
        entity.setTimezone( request.timezone() );
        entity.setOpeningDate( request.openingDate() );
        entity.setActive( request.active() );
    }

    private Long entityCompanyId(Region region) {
        Company company = region.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private String entityCompanyCode(Region region) {
        Company company = region.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getCode();
    }

    private Long entityParentId(Region region) {
        Region parent = region.getParent();
        if ( parent == null ) {
            return null;
        }
        return parent.getId();
    }

    private String entityParentName(Region region) {
        Region parent = region.getParent();
        if ( parent == null ) {
            return null;
        }
        return parent.getName();
    }

    private Long entityRegionId(Warehouse warehouse) {
        Region region = warehouse.getRegion();
        if ( region == null ) {
            return null;
        }
        return region.getId();
    }

    private String entityRegionCode(Warehouse warehouse) {
        Region region = warehouse.getRegion();
        if ( region == null ) {
            return null;
        }
        return region.getCode();
    }

    private Long entityRegionId1(Store store) {
        Region region = store.getRegion();
        if ( region == null ) {
            return null;
        }
        return region.getId();
    }

    private String entityRegionCode1(Store store) {
        Region region = store.getRegion();
        if ( region == null ) {
            return null;
        }
        return region.getCode();
    }

    private Long entityWarehouseId(Store store) {
        Warehouse warehouse = store.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        return warehouse.getId();
    }

    private String entityWarehouseCode(Store store) {
        Warehouse warehouse = store.getWarehouse();
        if ( warehouse == null ) {
            return null;
        }
        return warehouse.getCode();
    }
}
