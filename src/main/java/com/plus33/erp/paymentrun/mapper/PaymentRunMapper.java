/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Paymentrun Module
 * Package           : com.plus33.erp.paymentrun.mapper
 * File              : PaymentRunMapper.java
 * Purpose           : MapStruct Mapper converting between entities and DTOs in Paymentrun Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentRunController
 * Related Service   : PaymentRunService, PaymentRunServiceImpl
 * Related Repository: PaymentRunRepository
 * Related Entity    : PaymentRun
 * Related DTO       : PaymentRunInvoiceResponse, PaymentRunResponse, PaymentRunSupplierResultResponse, toInvoiceResponse, toResponse
 * Related Mapper    : PaymentRunMapper
 * Related DB Table  : payment_runs
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : PaymentRunService, PaymentRunServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * MapStruct Mapper for Paymentrun Module. Converts JPA entities to DTOs and vice versa. Generated at compile time. Inherits GlobalMapperConfig.
 ******************************************************************************/
package com.plus33.erp.paymentrun.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.paymentrun.dto.*;
import com.plus33.erp.paymentrun.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Paymentrun Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentRunMapper}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.paymentrun.mapper}</p>
 * <p><b>Layer  :</b> MapStruct Mapper: compile-time Entity to DTO conversion. No runtime reflection.</p>
 *
 * <p><b>Module Deps      :</b> Common, Paymentrun</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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