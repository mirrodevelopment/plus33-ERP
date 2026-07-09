/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.mapper
 * File              : CustomerMapper.java
 * Purpose           : MapStruct Mapper converting between entities and DTOs in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerController
 * Related Service   : CustomerService, CustomerServiceImpl
 * Related Repository: CustomerRepository
 * Related Entity    : Customer
 * Related DTO       : CustomerRequest, CustomerResponse, toResponse
 * Related Mapper    : CustomerMapper
 * Related DB Table  : customers
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : CustomerService, CustomerServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * MapStruct Mapper for Sales Module. Converts JPA entities to DTOs and vice versa. Generated at compile time. Inherits GlobalMapperConfig.
 ******************************************************************************/
package com.plus33.erp.sales.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.sales.dto.CustomerRequest;
import com.plus33.erp.sales.dto.CustomerResponse;
import com.plus33.erp.sales.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerMapper}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.mapper}</p>
 * <p><b>Layer  :</b> MapStruct Mapper: compile-time Entity to DTO conversion. No runtime reflection.</p>
 *
 * <p><b>Module Deps      :</b> Common, Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Mapper(config = GlobalMapperConfig.class)
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Customer toEntity(CustomerRequest request);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    CustomerResponse toResponse(Customer entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntity(CustomerRequest request, @MappingTarget Customer entity);
}