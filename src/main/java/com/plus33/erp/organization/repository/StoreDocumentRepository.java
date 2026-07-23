package com.plus33.erp.organization.repository;

import com.plus33.erp.organization.entity.StoreDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StoreDocumentRepository extends JpaRepository<StoreDocument, Long> {
    List<StoreDocument> findByStoreId(Long storeId);
}
