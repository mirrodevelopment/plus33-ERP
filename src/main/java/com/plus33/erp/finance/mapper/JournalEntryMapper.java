/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.mapper
 * File              : JournalEntryMapper.java
 * Purpose           : MapStruct Mapper converting between entities and DTOs in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: JournalEntryController
 * Related Service   : JournalEntryService, JournalEntryServiceImpl
 * Related Repository: JournalEntryRepository
 * Related Entity    : JournalEntry
 * Related DTO       : JournalEntryLineRequest, JournalEntryLineResponse, JournalEntryRequest, JournalEntryResponse, toLineResponse
 * Related Mapper    : JournalEntryMapper
 * Related DB Table  : journal_entrys
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : JournalEntryService, JournalEntryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * MapStruct Mapper for Finance Module. Converts JPA entities to DTOs and vice versa. Generated at compile time. Inherits GlobalMapperConfig.
 ******************************************************************************/
package com.plus33.erp.finance.mapper;

import com.plus33.erp.common.mapper.GlobalMapperConfig;
import com.plus33.erp.finance.dto.*;
import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.finance.entity.JournalEntryLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code JournalEntryMapper}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.mapper}</p>
 * <p><b>Layer  :</b> MapStruct Mapper: compile-time Entity to DTO conversion. No runtime reflection.</p>
 *
 * <p><b>Module Deps      :</b> Common, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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