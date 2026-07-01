package com.plus33.erp.crm.service;

import com.plus33.erp.crm.entity.CrmQuote;
import com.plus33.erp.crm.entity.CrmQuoteVersion;
import com.plus33.erp.crm.repository.CrmQuoteRepository;
import com.plus33.erp.crm.repository.CrmQuoteVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class CrmQuoteService {

    private final CrmQuoteRepository quoteRepo;
    private final CrmQuoteVersionRepository versionRepo;

    public CrmQuoteService(CrmQuoteRepository quoteRepo, CrmQuoteVersionRepository versionRepo) {
        this.quoteRepo = quoteRepo;
        this.versionRepo = versionRepo;
    }

    public CrmQuote createQuote(Long companyId, String quoteNum, Long customerId, Long oppId) {
        CrmQuote q = new CrmQuote();
        q.setCompanyId(companyId);
        q.setQuoteNumber(quoteNum);
        q.setCustomerId(customerId);
        q.setOpportunityId(oppId);
        q.setActiveVersionNumber(1);
        q.setStatus("DRAFT");
        CrmQuote saved = quoteRepo.save(q);

        CrmQuoteVersion version = new CrmQuoteVersion();
        version.setQuoteId(saved.getId());
        version.setVersionNumber(1);
        version.setStatus("DRAFT");
        version.setLocked(false);
        versionRepo.save(version);

        return saved;
    }

    public CrmQuoteVersion createNewVersion(Long quoteId, BigDecimal subtotal, BigDecimal discount, BigDecimal tax, BigDecimal total) {
        CrmQuote quote = quoteRepo.findById(quoteId)
                .orElseThrow(() -> new IllegalArgumentException("Quote not found: " + quoteId));

        int nextVer = quote.getActiveVersionNumber() + 1;
        quote.setActiveVersionNumber(nextVer);
        quoteRepo.save(quote);

        CrmQuoteVersion version = new CrmQuoteVersion();
        version.setQuoteId(quoteId);
        version.setVersionNumber(nextVer);
        version.setSubtotal(subtotal);
        version.setDiscountAmount(discount);
        version.setTaxAmount(tax);
        version.setTotalAmount(total);
        version.setStatus("DRAFT");
        version.setLocked(false);
        return versionRepo.save(version);
    }

    public void lockForApproval(Long quoteId, int versionNumber) {
        CrmQuoteVersion v = versionRepo.findByQuoteIdAndVersionNumber(quoteId, versionNumber)
                .orElseThrow(() -> new IllegalArgumentException("Version not found"));
        v.setStatus("APPROVAL_PENDING");
        v.setLocked(true);
        versionRepo.save(v);
    }
}
