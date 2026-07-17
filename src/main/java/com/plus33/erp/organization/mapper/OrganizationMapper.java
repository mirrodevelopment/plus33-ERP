/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.mapper
 * File              : OrganizationMapper.java
 * Purpose           : MapStruct Mapper converting between entities and DTOs in Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: OrganizationController
 * Related Service   : OrganizationService, OrganizationServiceImpl
 * Related Repository: OrganizationRepository
 * Related Entity    : Organization
 * Related DTO       : CompanyRequest, CompanyResponse, RegionRequest, RegionResponse, StoreRequest
 * Related Mapper    : OrganizationMapper
 * Related DB Table  : organizations
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : OrganizationService, OrganizationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * MapStruct Mapper for Organization Module. Converts JPA entities to DTOs and vice versa. Generated at compile time. Inherits GlobalMapperConfig.
 ******************************************************************************/
package com.plus33.erp.organization.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.organization.dto.*;
import com.plus33.erp.organization.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * <b>PLUS33 Coffee ERP -- Organization Module</b>
 *
 * <p><b>Class  :</b> {@code OrganizationMapper}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.mapper}</p>
 * <p><b>Layer  :</b> MapStruct Mapper: compile-time Entity to DTO conversion. No runtime reflection.</p>
 *
 * <p><b>Module Deps      :</b> Common, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Mapper(config = GlobalMapperConfig.class)
public interface OrganizationMapper {

    // --- Company Mappings ---
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "fiscalYearStartMonth", ignore = true)
    @Mapping(target = "fiscalYearStartDay", ignore = true)
    Company toEntity(CompanyRequest request);

    CompanyResponse toResponse(Company entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "fiscalYearStartMonth", ignore = true)
    @Mapping(target = "fiscalYearStartDay", ignore = true)
    void updateEntity(CompanyRequest request, @MappingTarget Company entity);

    // --- Region Mappings ---
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Region toEntity(RegionRequest request);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyCode", source = "company.code")
    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "parentName", source = "parent.name")
    RegionResponse toResponse(Region entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(RegionRequest request, @MappingTarget Region entity);

    // --- Warehouse Mappings ---
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "region", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Warehouse toEntity(WarehouseRequest request);

    @Mapping(target = "regionId", source = "region.id")
    @Mapping(target = "regionCode", source = "region.code")
    WarehouseResponse toResponse(Warehouse entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "region", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(WarehouseRequest request, @MappingTarget Warehouse entity);

    // --- Store Mappings ---
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "region", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Store toEntity(StoreRequest request);

    @Mapping(target = "regionId", source = "region.id")
    @Mapping(target = "regionCode", source = "region.code")
    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "warehouseCode", source = "warehouse.code")
    @Mapping(target = "employeeCount", ignore = true)
    @Mapping(target = "stockValue", ignore = true)
    StoreResponse toResponse(Store entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "region", ignore = true)
    @Mapping(target = "warehouse", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(StoreRequest request, @MappingTarget Store entity);

    // --- StoreSetting Mappings ---
    @Mapping(target = "storeId", source = "store.id")
    StoreSettingResponse toResponse(StoreSetting entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "store", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    StoreSetting toEntity(StoreSettingRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "store", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(StoreSettingRequest request, @MappingTarget StoreSetting entity);
}