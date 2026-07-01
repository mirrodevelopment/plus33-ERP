package com.plus33.erp.crm.repository;

import com.plus33.erp.crm.entity.CrmQuoteVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CrmQuoteVersionRepository extends JpaRepository<CrmQuoteVersion, Long> {
    List<CrmQuoteVersion> findByQuoteId(Long quoteId);
    Optional<CrmQuoteVersion> findByQuoteIdAndVersionNumber(Long quoteId, int versionNumber);
}
