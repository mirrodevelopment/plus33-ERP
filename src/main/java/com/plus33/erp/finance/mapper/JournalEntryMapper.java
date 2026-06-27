package com.plus33.erp.finance.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.finance.dto.*;
import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.finance.entity.JournalEntryLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class)
public interface JournalEntryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "entryNumber", ignore = true)
    @Mapping(target = "sourceModule", ignore = true)
    @Mapping(target = "sourceReference", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "reversalEntry", ignore = true)
    @Mapping(target = "closingEntry", ignore = true)
    @Mapping(target = "closingType", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "postedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lines", ignore = true)
    JournalEntry toEntity(JournalEntryRequest request);

    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "lines", source = "lines")
    JournalEntryResponse toResponse(JournalEntry entity);

    List<JournalEntryResponse> toResponseList(List<JournalEntry> list);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "journalEntry", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "dimensionSet", ignore = true)
    JournalEntryLine toLineEntity(JournalEntryLineRequest request);

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "accountCode", source = "account.accountCode")
    @Mapping(target = "accountName", source = "account.accountName")
    @Mapping(target = "dimensionSetId", source = "dimensionSet.id")
    JournalEntryLineResponse toLineResponse(JournalEntryLine entity);

    List<JournalEntryLineResponse> toLineResponseList(List<JournalEntryLine> list);
}
