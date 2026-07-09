/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.service
 * File              : CreditNoteServiceImpl.java
 * Purpose           : Business logic service layer for Sales Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CreditNoteController
 * Related Service   : CreditNoteServiceImpl
 * Related Repository: CreditNoteRepository
 * Related Entity    : CreditNote
 * Related DTO       : CreditNoteResponse, PageResponse, toResponse
 * Related Mapper    : CreditNoteMapper
 * Related DB Table  : credit_notes
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : CreditNoteController, CreditNoteServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Sales Module. Implements CreditNoteService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.sales.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.sales.dto.CreditNoteResponse;
import com.plus33.erp.sales.entity.CreditNote;
import com.plus33.erp.sales.entity.CreditNoteStatus;
import com.plus33.erp.sales.mapper.CreditNoteMapper;
import com.plus33.erp.sales.repository.CreditNoteRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CreditNoteServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Sales Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * CreditNoteController
 *   --> CreditNoteServiceImpl (this)
 *   --> Validate business rules
 *   --> CreditNoteRepository (read/write 'credit_notes')
 *   --> CreditNoteMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code credit_notes}</p>
 * <p><b>Module Deps      :</b> Common, Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional(readOnly = true)
public class CreditNoteServiceImpl implements CreditNoteService {

    private final CreditNoteRepository creditNoteRepository;
    private final CreditNoteMapper creditNoteMapper;

    public CreditNoteServiceImpl(CreditNoteRepository creditNoteRepository, CreditNoteMapper creditNoteMapper) {
        this.creditNoteRepository = creditNoteRepository;
        this.creditNoteMapper = creditNoteMapper;
    }

    /**
     * Retrieves a single credit note by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the CreditNoteResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves a single credit note by id by its identifier.
     *
     * @param id the unique database ID of the resource
     * @return the CreditNoteResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public CreditNoteResponse getCreditNoteById(Long id) {
        CreditNote creditNote = creditNoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credit Note not found with ID: " + id));
        return creditNoteMapper.toResponse(creditNote);
    }

    /**
     * Returns a filtered paginated list of credit notes records.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param customerId the customerId input value
     * @param creditNoteNumber the creditNoteNumber input value
     * @param status status filter for narrowing query results
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    /**
     * Returns a filtered paginated list of credit notes records.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param customerId the customerId input value
     * @param creditNoteNumber the creditNoteNumber input value
     * @param status status filter for narrowing query results
     * @param pageable Spring Pageable (page, size, sort) from query parameters
     * @return the PageResponse result
     */
    @Override
    public PageResponse<CreditNoteResponse> searchCreditNotes(Long companyId, Long customerId, String creditNoteNumber, String status, Pageable pageable) {
        Specification<CreditNote> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (companyId != null) {
                predicates.add(cb.equal(root.get("company").get("id"), companyId));
            }
            if (customerId != null) {
                predicates.add(cb.equal(root.get("customer").get("id"), customerId));
            }
            if (creditNoteNumber != null && !creditNoteNumber.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("creditNoteNumber")), "%" + creditNoteNumber.toLowerCase() + "%"));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), CreditNoteStatus.valueOf(status.toUpperCase())));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<CreditNote> page = creditNoteRepository.findAll(spec, pageable);
        List<CreditNoteResponse> content = page.getContent().stream()
                .map(creditNoteMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}