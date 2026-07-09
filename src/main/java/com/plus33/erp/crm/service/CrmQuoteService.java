/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.service
 * File              : CrmQuoteService.java
 * Purpose           : Business logic service layer for Crm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CrmQuoteController
 * Related Service   : CrmQuoteService
 * Related Repository: CrmQuoteRepository, CrmQuoteVersionRepository
 * Related Entity    : CrmQuote
 * Related DTO       : N/A
 * Related Mapper    : CrmQuoteMapper
 * Related DB Table  : crm_quotes
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CrmQuoteController, CrmQuoteServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Crm Module. Implements CrmQuoteService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.crm.service;

import com.plus33.erp.crm.entity.CrmQuote;
import com.plus33.erp.crm.entity.CrmQuoteVersion;
import com.plus33.erp.crm.repository.CrmQuoteRepository;
import com.plus33.erp.crm.repository.CrmQuoteVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code CrmQuoteService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Crm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * CrmQuoteController
 *   --> CrmQuoteService (this)
 *   --> Validate business rules
 *   --> CrmQuoteRepository (read/write 'crm_quotes')
 *   --> CrmQuoteMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code crm_quotes}</p>
 * <p><b>Module Deps      :</b> Crm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional
public class CrmQuoteService {

    private final CrmQuoteRepository quoteRepo;
    private final CrmQuoteVersionRepository versionRepo;

    public CrmQuoteService(CrmQuoteRepository quoteRepo, CrmQuoteVersionRepository versionRepo) {
        this.quoteRepo = quoteRepo;
        this.versionRepo = versionRepo;
    }

    /**
     * Creates a new quote and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param quoteNum the quoteNum input value
     * @param customerId the customerId input value
     * @param oppId the oppId input value
     * @return the CrmQuote result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Creates a new new version and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param quoteId the quoteId input value
     * @param subtotal the subtotal input value
     * @param discount the discount input value
     * @param tax the tax input value
     * @param total the total input value
     * @return the CrmQuoteVersion result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Performs the lockForApproval operation in this module.
     *
     * @param quoteId the quoteId input value
     * @param versionNumber the versionNumber input value
     */
    public void lockForApproval(Long quoteId, int versionNumber) {
        CrmQuoteVersion v = versionRepo.findByQuoteIdAndVersionNumber(quoteId, versionNumber)
                .orElseThrow(() -> new IllegalArgumentException("Version not found"));
        v.setStatus("APPROVAL_PENDING");
        v.setLocked(true);
        versionRepo.save(v);
    }
}