package com.plus33.erp.finance.mapper;

import com.plus33.erp.finance.budget.entity.BudgetDimensionSet;
import com.plus33.erp.finance.dto.JournalEntryLineRequest;
import com.plus33.erp.finance.dto.JournalEntryLineResponse;
import com.plus33.erp.finance.dto.JournalEntryRequest;
import com.plus33.erp.finance.dto.JournalEntryResponse;
import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.finance.entity.JournalEntryLine;
import com.plus33.erp.organization.entity.Company;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-13T18:32:48+0530",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.17 (Eclipse Adoptium)"
)
@Component
public class JournalEntryMapperImpl implements JournalEntryMapper {

    @Override
    public JournalEntry toEntity(JournalEntryRequest request) {
        if ( request == null ) {
            return null;
        }

        JournalEntry.JournalEntryBuilder journalEntry = JournalEntry.builder();

        journalEntry.entryDate( request.entryDate() );
        journalEntry.description( request.description() );
        journalEntry.currencyCode( request.currencyCode() );

        return journalEntry.build();
    }

    @Override
    public JournalEntryResponse toResponse(JournalEntry entity) {
        if ( entity == null ) {
            return null;
        }

        Long companyId = null;
        List<JournalEntryLineResponse> lines = null;
        Long id = null;
        String entryNumber = null;
        LocalDate entryDate = null;
        String description = null;
        String sourceModule = null;
        String sourceReference = null;
        String status = null;
        String currencyCode = null;
        LocalDateTime postedAt = null;

        companyId = entityCompanyId( entity );
        lines = toLineResponseList( entity.getLines() );
        id = entity.getId();
        entryNumber = entity.getEntryNumber();
        entryDate = entity.getEntryDate();
        description = entity.getDescription();
        sourceModule = entity.getSourceModule();
        sourceReference = entity.getSourceReference();
        status = entity.getStatus();
        currencyCode = entity.getCurrencyCode();
        postedAt = entity.getPostedAt();

        JournalEntryResponse journalEntryResponse = new JournalEntryResponse( id, entryNumber, companyId, entryDate, description, sourceModule, sourceReference, status, currencyCode, postedAt, lines );

        return journalEntryResponse;
    }

    @Override
    public List<JournalEntryResponse> toResponseList(List<JournalEntry> list) {
        if ( list == null ) {
            return null;
        }

        List<JournalEntryResponse> list1 = new ArrayList<JournalEntryResponse>( list.size() );
        for ( JournalEntry journalEntry : list ) {
            list1.add( toResponse( journalEntry ) );
        }

        return list1;
    }

    @Override
    public JournalEntryLine toLineEntity(JournalEntryLineRequest request) {
        if ( request == null ) {
            return null;
        }

        JournalEntryLine.JournalEntryLineBuilder journalEntryLine = JournalEntryLine.builder();

        journalEntryLine.debitAmount( request.debitAmount() );
        journalEntryLine.creditAmount( request.creditAmount() );

        return journalEntryLine.build();
    }

    @Override
    public JournalEntryLineResponse toLineResponse(JournalEntryLine entity) {
        if ( entity == null ) {
            return null;
        }

        Long accountId = null;
        String accountCode = null;
        String accountName = null;
        Long dimensionSetId = null;
        Long id = null;
        BigDecimal debitAmount = null;
        BigDecimal creditAmount = null;

        accountId = entityAccountId( entity );
        accountCode = entityAccountAccountCode( entity );
        accountName = entityAccountAccountName( entity );
        dimensionSetId = entityDimensionSetId( entity );
        id = entity.getId();
        debitAmount = entity.getDebitAmount();
        creditAmount = entity.getCreditAmount();

        JournalEntryLineResponse journalEntryLineResponse = new JournalEntryLineResponse( id, accountId, accountCode, accountName, debitAmount, creditAmount, dimensionSetId );

        return journalEntryLineResponse;
    }

    @Override
    public List<JournalEntryLineResponse> toLineResponseList(List<JournalEntryLine> list) {
        if ( list == null ) {
            return null;
        }

        List<JournalEntryLineResponse> list1 = new ArrayList<JournalEntryLineResponse>( list.size() );
        for ( JournalEntryLine journalEntryLine : list ) {
            list1.add( toLineResponse( journalEntryLine ) );
        }

        return list1;
    }

    private Long entityCompanyId(JournalEntry journalEntry) {
        Company company = journalEntry.getCompany();
        if ( company == null ) {
            return null;
        }
        return company.getId();
    }

    private Long entityAccountId(JournalEntryLine journalEntryLine) {
        Account account = journalEntryLine.getAccount();
        if ( account == null ) {
            return null;
        }
        return account.getId();
    }

    private String entityAccountAccountCode(JournalEntryLine journalEntryLine) {
        Account account = journalEntryLine.getAccount();
        if ( account == null ) {
            return null;
        }
        return account.getAccountCode();
    }

    private String entityAccountAccountName(JournalEntryLine journalEntryLine) {
        Account account = journalEntryLine.getAccount();
        if ( account == null ) {
            return null;
        }
        return account.getAccountName();
    }

    private Long entityDimensionSetId(JournalEntryLine journalEntryLine) {
        BudgetDimensionSet dimensionSet = journalEntryLine.getDimensionSet();
        if ( dimensionSet == null ) {
            return null;
        }
        return dimensionSet.getId();
    }
}
