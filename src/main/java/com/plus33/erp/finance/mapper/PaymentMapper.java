package com.plus33.erp.finance.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.finance.dto.*;
import com.plus33.erp.finance.entity.Payment;
import com.plus33.erp.finance.entity.PaymentAllocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

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
