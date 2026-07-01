package com.plus33.erp.crm.repository;

import com.plus33.erp.crm.entity.CrmQuoteVersionLine;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CrmQuoteVersionLineRepository extends JpaRepository<CrmQuoteVersionLine, Long> {
    List<CrmQuoteVersionLine> findByQuoteVersionId(Long quoteVersionId);
}
