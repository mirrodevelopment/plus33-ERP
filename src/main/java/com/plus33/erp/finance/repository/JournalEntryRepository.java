package com.plus33.erp.finance.repository;

import com.plus33.erp.finance.entity.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    Optional<JournalEntry> findByCompanyIdAndEntryNumber(Long companyId, String entryNumber);
    List<JournalEntry> findByCompanyId(Long companyId);
}
