package com.plus33.erp.sales.mapper;

import com.plus33.erp.sales.dto.CreditNoteItemResponse;
import com.plus33.erp.sales.dto.CreditNoteResponse;
import com.plus33.erp.sales.entity.CreditNote;
import com.plus33.erp.sales.entity.CreditNoteItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
