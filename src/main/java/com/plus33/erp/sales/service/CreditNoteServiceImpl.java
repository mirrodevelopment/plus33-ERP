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

@Service
@Transactional(readOnly = true)
public class CreditNoteServiceImpl implements CreditNoteService {

    private final CreditNoteRepository creditNoteRepository;
    private final CreditNoteMapper creditNoteMapper;

    public CreditNoteServiceImpl(CreditNoteRepository creditNoteRepository, CreditNoteMapper creditNoteMapper) {
        this.creditNoteRepository = creditNoteRepository;
        this.creditNoteMapper = creditNoteMapper;
    }

    @Override
    public CreditNoteResponse getCreditNoteById(Long id) {
        CreditNote creditNote = creditNoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credit Note not found with ID: " + id));
        return creditNoteMapper.toResponse(creditNote);
    }

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
