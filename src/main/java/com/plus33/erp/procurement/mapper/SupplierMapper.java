package com.plus33.erp.procurement.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.procurement.dto.SupplierRequest;
import com.plus33.erp.procurement.dto.SupplierResponse;
import com.plus33.erp.procurement.entity.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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
