package com.plus33.erp.workforce.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.workforce.dto.EmployeeRequest;
import com.plus33.erp.workforce.dto.EmployeeResponse;
import com.plus33.erp.workforce.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = GlobalMapperConfig.class)
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Employee toEntity(EmployeeRequest request);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "companyCode", source = "company.code")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "regionId", ignore = true)
    @Mapping(target = "regionName", ignore = true)
    @Mapping(target = "regionCode", ignore = true)
    @Mapping(target = "storeId", ignore = true)
    @Mapping(target = "storeName", ignore = true)
    @Mapping(target = "storeCode", ignore = true)
    EmployeeResponse toResponse(Employee entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(EmployeeRequest request, @MappingTarget Employee entity);
}
