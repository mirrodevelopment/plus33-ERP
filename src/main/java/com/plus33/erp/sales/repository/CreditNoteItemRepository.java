package com.plus33.erp.sales.repository;

import com.plus33.erp.sales.entity.CreditNoteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditNoteItemRepository extends JpaRepository<CreditNoteItem, Long> {
}
