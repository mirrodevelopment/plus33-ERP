package com.plus33.erp.paymentrun.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.paymentrun.dto.*;
import com.plus33.erp.paymentrun.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class)
public interface PaymentRunMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "filterSupplierId", source = "filterSupplier.id")
    @Mapping(target = "approvedByEmail", source = "approvedBy.email")
    @Mapping(target = "executedByEmail", source = "executedBy.email")
    @Mapping(target = "cancelledByEmail", source = "cancelledBy.email")
    @Mapping(target = "createdByEmail", source = "createdBy.email")
    @Mapping(target = "invoices", source = "invoices")
    @Mapping(target = "supplierResults", source = "supplierResults")
    PaymentRunResponse toResponse(PaymentRun entity);

    List<PaymentRunResponse> toResponseList(List<PaymentRun> list);

    @Mapping(target = "supplierInvoiceId", source = "supplierInvoice.id")
    @Mapping(target = "invoiceNumber", source = "supplierInvoice.invoiceNumber")
    @Mapping(target = "supplierId", source = "supplierInvoice.supplier.id")
    @Mapping(target = "supplierName", source = "supplierInvoice.supplier.name")
    PaymentRunInvoiceResponse toInvoiceResponse(PaymentRunInvoice entity);

    List<PaymentRunInvoiceResponse> toInvoiceResponseList(List<PaymentRunInvoice> list);

    @Mapping(target = "supplierId", source = "supplier.id")
    @Mapping(target = "supplierName", source = "supplier.name")
    @Mapping(target = "paymentId", source = "payment.id")
    @Mapping(target = "paymentNumber", source = "payment.paymentNumber")
    PaymentRunSupplierResultResponse toSupplierResultResponse(PaymentRunSupplierResult entity);

    List<PaymentRunSupplierResultResponse> toSupplierResultResponseList(List<PaymentRunSupplierResult> list);
}
