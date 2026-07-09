/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.mapper
 * File              : SupplierMapper.java
 * Purpose           : MapStruct Mapper converting between entities and DTOs in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierController
 * Related Service   : SupplierService, SupplierServiceImpl
 * Related Repository: SupplierRepository
 * Related Entity    : Supplier
 * Related DTO       : SupplierRequest, SupplierResponse, toResponse
 * Related Mapper    : SupplierMapper
 * Related DB Table  : suppliers
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : SupplierService, SupplierServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * MapStruct Mapper for Procurement Module. Converts JPA entities to DTOs and vice versa. Generated at compile time. Inherits GlobalMapperConfig.
 ******************************************************************************/
package com.plus33.erp.procurement.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.procurement.dto.SupplierRequest;
import com.plus33.erp.procurement.dto.SupplierResponse;
import com.plus33.erp.procurement.entity.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierMapper}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.mapper}</p>
 * <p><b>Layer  :</b> MapStruct Mapper: compile-time Entity to DTO conversion. No runtime reflection.</p>
 *
 * <p><b>Module Deps      :</b> Common, Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Mapper(config = GlobalMapperConfig.class)
public interface SupplierMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "bankName", ignore = true)
    @Mapping(target = "bankAccountNumber", ignore = true)
    @Mapping(target = "swiftCode", ignore = true)
    @Mapping(target = "iban", ignore = true)
    Supplier toEntity(SupplierRequest request);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyCode", source = "company.code")
    SupplierResponse toResponse(Supplier entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "bankName", ignore = true)
    @Mapping(target = "bankAccountNumber", ignore = true)
    @Mapping(target = "swiftCode", ignore = true)
    @Mapping(target = "iban", ignore = true)
    void updateEntity(SupplierRequest request, @MappingTarget Supplier entity);
}