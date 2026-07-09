/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.mapper
 * File              : PaymentMapper.java
 * Purpose           : MapStruct Mapper converting between entities and DTOs in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentController
 * Related Service   : PaymentService, PaymentServiceImpl
 * Related Repository: PaymentRepository
 * Related Entity    : Payment
 * Related DTO       : PaymentAllocationRequest, PaymentAllocationResponse, PaymentRequest, PaymentResponse, toAllocationResponse
 * Related Mapper    : PaymentMapper
 * Related DB Table  : payments
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : PaymentService, PaymentServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * MapStruct Mapper for Finance Module. Converts JPA entities to DTOs and vice versa. Generated at compile time. Inherits GlobalMapperConfig.
 ******************************************************************************/
package com.plus33.erp.finance.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.finance.dto.*;
import com.plus33.erp.finance.entity.Payment;
import com.plus33.erp.finance.entity.PaymentAllocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentMapper}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.mapper}</p>
 * <p><b>Layer  :</b> MapStruct Mapper: compile-time Entity to DTO conversion. No runtime reflection.</p>
 *
 * <p><b>Module Deps      :</b> Common, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Mapper(config = GlobalMapperConfig.class)
public interface PaymentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "paymentNumber", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "supplier", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "journalEntry", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "cancelledAt", ignore = true)
    @Mapping(target = "cancelledBy", ignore = true)
    @Mapping(target = "cancellationReason", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "allocations", ignore = true)
    @Mapping(target = "paymentType", ignore = true)
    @Mapping(target = "paymentBatchId", ignore = true)
    Payment toEntity(PaymentRequest request);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.name")
    @Mapping(target = "supplierId", source = "supplier.id")
    @Mapping(target = "supplierName", source = "supplier.name")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.name")
    @Mapping(target = "journalEntryId", source = "journalEntry.id")
    @Mapping(target = "journalEntryNumber", source = "journalEntry.entryNumber")
    @Mapping(target = "cancelledByUserId", source = "cancelledBy.id")
    @Mapping(target = "cancelledByUserName", source = "cancelledBy.firstName")
    @Mapping(target = "createdByUserId", source = "createdBy.id")
    @Mapping(target = "createdByUserName", source = "createdBy.firstName")
    PaymentResponse toResponse(Payment entity);

    List<PaymentResponse> toResponseList(List<Payment> list);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "payment", ignore = true)
    @Mapping(target = "supplierInvoice", ignore = true)
    @Mapping(target = "customerInvoice", ignore = true)
    PaymentAllocation toAllocationEntity(PaymentAllocationRequest request);

    @Mapping(target = "supplierInvoiceId", source = "supplierInvoice.id")
    @Mapping(target = "supplierInvoiceNumber", source = "supplierInvoice.invoiceNumber")
    @Mapping(target = "customerInvoiceId", source = "customerInvoice.id")
    @Mapping(target = "customerInvoiceNumber", source = "customerInvoice.invoiceNumber")
    PaymentAllocationResponse toAllocationResponse(PaymentAllocation entity);

    List<PaymentAllocationResponse> toAllocationResponseList(List<PaymentAllocation> list);
}