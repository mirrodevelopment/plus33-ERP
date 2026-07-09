/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.mapper
 * File              : CreditNoteMapper.java
 * Purpose           : MapStruct Mapper converting between entities and DTOs in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CreditNoteController
 * Related Service   : CreditNoteService, CreditNoteServiceImpl
 * Related Repository: CreditNoteRepository
 * Related Entity    : CreditNote
 * Related DTO       : CreditNoteItemResponse, CreditNoteResponse, toItemResponse, toResponse
 * Related Mapper    : CreditNoteMapper
 * Related DB Table  : credit_notes
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CreditNoteService, CreditNoteServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * MapStruct Mapper for Sales Module. Converts JPA entities to DTOs and vice versa. Generated at compile time. Inherits GlobalMapperConfig.
 ******************************************************************************/
package com.plus33.erp.sales.mapper;

import com.plus33.erp.sales.dto.CreditNoteItemResponse;
import com.plus33.erp.sales.dto.CreditNoteResponse;
import com.plus33.erp.sales.entity.CreditNote;
import com.plus33.erp.sales.entity.CreditNoteItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CreditNoteMapper}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.mapper}</p>
 * <p><b>Layer  :</b> MapStruct Mapper: compile-time Entity to DTO conversion. No runtime reflection.</p>
 *
 * <p><b>Module Deps      :</b> Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Mapper(componentModel = "spring")
public interface CreditNoteMapper {

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.name")
    @Mapping(target = "customerCode", source = "customer.code")
    @Mapping(target = "customerReturnId", source = "customerReturn.id")
    @Mapping(target = "customerReturnNumber", source = "customerReturn.returnNumber")
    @Mapping(target = "customerInvoiceId", source = "customerInvoice.id")
    @Mapping(target = "customerInvoiceNumber", source = "customerInvoice.invoiceNumber")
    @Mapping(target = "journalEntryId", source = "journalEntry.id")
    @Mapping(target = "journalEntryNumber", source = "journalEntry.entryNumber")
    @Mapping(target = "createdById", source = "createdBy.id")
    @Mapping(target = "createdByName", source = "createdBy.firstName")
    @Mapping(target = "approvedById", source = "approvedBy.id")
    @Mapping(target = "approvedByName", source = "approvedBy.firstName")
    @Mapping(target = "cancelledById", source = "cancelledBy.id")
    @Mapping(target = "cancelledByName", source = "cancelledBy.firstName")
    CreditNoteResponse toResponse(CreditNote creditNote);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productCode", source = "product.code")
    CreditNoteItemResponse toItemResponse(CreditNoteItem item);
}