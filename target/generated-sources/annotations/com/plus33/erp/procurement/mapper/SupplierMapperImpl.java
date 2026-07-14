package com.plus33.erp.procurement.mapper;

import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.procurement.dto.SupplierRequest;
import com.plus33.erp.procurement.dto.SupplierResponse;
import com.plus33.erp.procurement.entity.Supplier;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-14T10:25:48+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class SupplierMapperImpl implements SupplierMapper {

    @Override
    public Supplier toEntity(SupplierRequest request) {
        if ( request == null ) {
            return null;
        }

        Supplier.SupplierBuilder supplier = Supplier.builder();

        supplier.code( request.code() );
        supplier.name( request.name() );
        supplier.contactPerson( request.contactPerson() );
        supplier.email( request.email() );
        supplier.phone( request.phone() );
        supplier.address( request.address() );
        supplier.taxNumber( request.taxNumber() );
        supplier.active( request.active() );

        return supplier.build();
    }

    @Override
    public SupplierResponse toResponse(Supplier entity) {
        if ( entity == null ) {
            return null;
        }

        Long companyId = null;
        String companyCode = null;
        Long id = null;
        String code = null;
        String name = null;
        String contactPerson = null;
        String email = null;
        String phone = null;
        String address = null;
        String taxNumber = null;
        Boolean active = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        companyId = entityCompanyId( entity );
        companyCode = entityCompanyCode( entity );
        id = entity.getId();
        code = entity.getCode();
        name = entity.getName();
        contactPerson = entity.getContactPerson();
        email = entity.getEmail();
        phone = entity.getPhone();
        address = entity.getAddress();
        taxNumber = entity.getTaxNumber();
        active = entity.getActive();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();

        SupplierResponse supplierResponse = new SupplierResponse( id, companyId, companyCode, code, name, contactPerson, email, phone, address, taxNumber, active, createdAt, updatedAt );

        return supplierResponse;
    }

    @Override
    public void updateEntity(SupplierRequest request, Supplier entity) {
        if ( request == null ) {
            return;
        }

        entity.setCode( request.code() );
        entity.setName( request.name() );
        entity.setContactPerson( request.contactPerson() );
        entity.setEmail( request.email() );
        entity.setPhone( request.phone() );
        entity.setAddress( request.address() );
        entity.setTaxNumber( request.taxNumber() );
        entity.setActive( request.active() );
    }

    private Long entityCompanyId(Supplier supplier) {
        Company company = supplier.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private String entityCompanyCode(Supplier supplier) {
        Company company = supplier.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getCode();
    }
}
