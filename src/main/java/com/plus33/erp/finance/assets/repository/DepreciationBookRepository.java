package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.DepreciationBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepreciationBookRepository extends JpaRepository<DepreciationBook, Long> {
    Optional<DepreciationBook> findByCompanyIdAndCode(Long companyId, String code);
    List<DepreciationBook> findAllByCompanyId(Long companyId);
}
