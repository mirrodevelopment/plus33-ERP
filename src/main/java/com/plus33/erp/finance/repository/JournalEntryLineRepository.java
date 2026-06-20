package com.plus33.erp.finance.repository;

import com.plus33.erp.finance.entity.JournalEntryLine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JournalEntryLineRepository extends JpaRepository<JournalEntryLine, Long> {
    List<JournalEntryLine> findByJournalEntryId(Long journalEntryId);
}
