package com.plus33.erp.sales.mapper;

import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.sales.dto.CustomerRequest;
import com.plus33.erp.sales.dto.CustomerResponse;
import com.plus33.erp.sales.entity.Customer;
import com.plus33.erp.sales.entity.CustomerStatus;
import com.plus33.erp.sales.entity.CustomerType;
import com.plus33.erp.sales.entity.TaxProfile;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-13T18:32:48+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public Customer toEntity(CustomerRequest request) {
        if ( request == null ) {
            return null;
        }

        Customer.CustomerBuilder customer = Customer.builder();

        customer.code( request.code() );
        customer.name( request.name() );
        customer.customerType( request.customerType() );
        customer.status( request.status() );
        customer.contactPerson( request.contactPerson() );
        customer.email( request.email() );
        customer.phone( request.phone() );
        customer.billingAddress( request.billingAddress() );
        customer.shippingAddress( request.shippingAddress() );
        customer.taxNumber( request.taxNumber() );
        customer.taxProfile( request.taxProfile() );
        customer.creditLimit( request.creditLimit() );
        customer.outstandingBalance( request.outstandingBalance() );
        customer.pricingTier( request.pricingTier() );
        customer.discountRate( request.discountRate() );
        customer.paymentTermsDays( request.paymentTermsDays() );
        customer.currencyCode( request.currencyCode() );

        return customer.build();
    }

    @Override
    public CustomerResponse toResponse(Customer entity) {
        if ( entity == null ) {
            return null;
        }

        Long companyId = null;
        String companyName = null;
        Long id = null;
        String code = null;
        String name = null;
        CustomerType customerType = null;
        CustomerStatus status = null;
        String contactPerson = null;
        String email = null;
        String phone = null;
        String billingAddress = null;
        String shippingAddress = null;
        String taxNumber = null;
        TaxProfile taxProfile = null;
        BigDecimal creditLimit = null;
        BigDecimal outstandingBalance = null;
        String pricingTier = null;
        BigDecimal discountRate = null;
        Integer paymentTermsDays = null;
        String currencyCode = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;
        Long version = null;

        companyId = entityCompanyId( entity );
        companyName = entityCompanyName( entity );
        id = entity.getId();
        code = entity.getCode();
        name = entity.getName();
        customerType = entity.getCustomerType();
        status = entity.getStatus();
        contactPerson = entity.getContactPerson();
        email = entity.getEmail();
        phone = entity.getPhone();
        billingAddress = entity.getBillingAddress();
        shippingAddress = entity.getShippingAddress();
        taxNumber = entity.getTaxNumber();
        taxProfile = entity.getTaxProfile();
        creditLimit = entity.getCreditLimit();
        outstandingBalance = entity.getOutstandingBalance();
        pricingTier = entity.getPricingTier();
        discountRate = entity.getDiscountRate();
        paymentTermsDays = entity.getPaymentTermsDays();
        currencyCode = entity.getCurrencyCode();
        createdAt = entity.getCreatedAt();
        updatedAt = entity.getUpdatedAt();
        version = entity.getVersion();

        CustomerResponse customerResponse = new CustomerResponse( id, companyId, companyName, code, name, customerType, status, contactPerson, email, phone, billingAddress, shippingAddress, taxNumber, taxProfile, creditLimit, outstandingBalance, pricingTier, discountRate, paymentTermsDays, currencyCode, createdAt, updatedAt, version );

        return customerResponse;
    }

    @Override
    public void updateEntity(CustomerRequest request, Customer entity) {
        if ( request == null ) {
            return;
        }

        entity.setCode( request.code() );
        entity.setName( request.name() );
        entity.setCustomerType( request.customerType() );
        entity.setStatus( request.status() );
        entity.setContactPerson( request.contactPerson() );
        entity.setEmail( request.email() );
        entity.setPhone( request.phone() );
        entity.setBillingAddress( request.billingAddress() );
        entity.setShippingAddress( request.shippingAddress() );
        entity.setTaxNumber( request.taxNumber() );
        entity.setTaxProfile( request.taxProfile() );
        entity.setCreditLimit( request.creditLimit() );
        entity.setOutstandingBalance( request.outstandingBalance() );
        entity.setPricingTier( request.pricingTier() );
        entity.setDiscountRate( request.discountRate() );
        entity.setPaymentTermsDays( request.paymentTermsDays() );
        entity.setCurrencyCode( request.currencyCode() );
    }

    private Long entityCompanyId(Customer customer) {
        Company company = customer.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private String entityCompanyName(Customer customer) {
        Company company = customer.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getName();
    }
}
