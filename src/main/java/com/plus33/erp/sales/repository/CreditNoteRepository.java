package com.plus33.erp.sales.repository;

import com.plus33.erp.sales.entity.CreditNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditNoteRepository extends JpaRepository<CreditNote, Long>, JpaSpecificationExecutor<CreditNote> {
    java.util.Optional<CreditNote> findByClientReferenceId(java.util.UUID clientReferenceId);
    Optional<CreditNote> findByCompanyIdAndCreditNoteNumber(Long companyId, String creditNoteNumber);

    @Query(value = "SELECT nextval('credit_note_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}
