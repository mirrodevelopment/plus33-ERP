package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.BomLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BomLineRepository extends JpaRepository<BomLine, Long> {

    List<BomLine> findByBomHeaderIdOrderBySortSequenceAscLineNumberAsc(Long bomHeaderId);

    @Query("""
        SELECT bl FROM BomLine bl
        WHERE bl.bomHeader.id = :bomHeaderId
          AND bl.componentProduct.id = :productId
    """)
    List<BomLine> findByBomHeaderIdAndComponentProductId(
            @Param("bomHeaderId") Long bomHeaderId,
            @Param("productId") Long productId);

    void deleteByBomHeaderId(Long bomHeaderId);
}
