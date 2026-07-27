package com.plus33.erp.support.repository;

import com.plus33.erp.support.entity.KnowledgeBaseFaq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeBaseFaqRepository extends JpaRepository<KnowledgeBaseFaq, Long> {
    List<KnowledgeBaseFaq> findAllByOrderBySortOrderAsc();
}
